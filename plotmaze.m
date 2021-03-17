function f = plotmaze(varargin)
% f = plotmaze(varargin)
% live plot function that records how well the animals are doing on the task.
%
% TODO add getestprobcorrect output to a panel in the output! This
% would be super helpful
% TODO maybe to save time, think about changing the clf to selective
% cla of the graphs that need to be changed (not sure if this will
% interfere with anything, but hopefully not).
% TODO - functionalize the individual plots
%      - prepare plotperf for autosegmenting
%       types of performance to plot (will help
%       for future, checking performance of rule sets)

%% READ IN MAZE GLOBALS
offlinemode = false;
global perf
if isempty(perf) && nargin>0
  perf = varargin{1};
end
if nargin > 1
  maze = varargin{2};
end
if nargin > 2
  state= varargin{3};
end
if nargin > 3
  offlinemode = varargin{4};
end
if nargin < 4 ; offlinemode = false ; end
global const

% Optional keyword args?
prettyplot = false;
notstatespace = false;
table_statespaces = true;
include_past = true; % setting up to be capable of including far previous activity
char_function_input = cellfun(@ischar,varargin);
if any(char_function_input)
    optlistassign(who, varargin{ find(char_function_input, 1, 'first'):end });
end


f=sfigure(1);
tic

lnkx = gobjects(1,3);

% OVERALL CORRECT ERROR
ax = select_axis('upper left');
plot_correrr_sumamry(ax)

% PLATFORM CORRECT ERROR
ax = select_axis('upper right')
plot_performance_per_platform(ax); 

% CLOCK
ax = select_axis('middle right');
plot_time(ax, offlinemode)

% REWARD ERROR POKE RECORD
lnkx(1) = select_axis('middle left');
plot_rewarderror_dynamics(lnkx(1));

% CUMULATIVE STATS
%ax = select_axis('lower left');
%plot_cumulative_statistics(ax);
%toc; 
% STATESPACE
if ~notstatespace && exist('state','var') && (offlinemode==true || state.blocklockout==false)
  ax = select_axis('all lower left');
  lnkx(2) = ax;
  cla(ax,'reset')
  c = 1;
  if any(perf.table.wm == 1)
    calc_wm = true;
    c=c+1;
  else
    calc_wm = false;
  end
  we_have_a_sequence     = const.sequence>1 || const.train.allowedcorrect >1;
  more_than_one_seq_corr = any(perf.table.seqfin == 1);
  if we_have_a_sequence && more_than_one_seq_corr
    calc_seqfin = true;
    c=c+1;
  else
    calc_seqfin = false;
  end
  colors = cmocean('haline', 3+2);
  colors = colors(2:end-1,:);
  obj = plot_statespace_groupby(ax, perf.table, 'color', colors(1,:),'background_prob',0.25);
  tobj = {'overall'};
  if table_statespaces && calc_wm
      obj = [obj plot_statespace_groupby(ax, perf.table(perf.table.wm==1,:), ...
        'color', colors(2,:),'background_prob',0.25)];
      tobj{end+1} = 'working memory';
  end
  
  if table_statespaces && calc_seqfin
      obj = [obj plot_statespace_groupby(ax, perf.table(perf.table.seqfin==1,:), ...
        ... 'color', colors(3,:), 'background_prob', (perf.correct/(perf.correct+perf.incorrect))^(const.sequence*const.train.allowedcorrect))];
        'color', colors(3,:), 'background_prob', (1/numel(maze.platforms))^(const.sequence*const.train.allowedcorrect))];
      tobj{end+1} = 'sequence completion';
  end
  filt = arrayfun(@(x) isa(x,'matlab.graphics.chart.primitive.Line'), obj);
  L=legend(ax, obj(filt), tobj(filt),'Location','southeast','Orientation','vertical','fontsize',8);
  title(L,'Task Subset','fontsize',8);
  xlabel(ax, 'Minutes','fontsize',10);
  ylabel(ax, 'Performance','fontsize',8);

end

statespace_wells = true;
if ~notstatespace && statespace_wells
  ax = select_axis('all lower right');
  cla(ax)
  lnkx(3) = ax;
  obj= plot_statespace_groupby(ax, perf.table, ...
    'group', 'trialplat',...;
    'cmap', 'dense',...
    'add_index', true);
  tobj = arrayfun(@num2str, maze.platforms, 'UniformOutput', false);
  filt = arrayfun(@(x) isa(x, 'matlab.graphics.chart.primitive.Line'), obj);
  L=legend(ax, obj(filt), tobj(filt), 'Location', 'best','Orientation','horizontal','fontsize',8);
  title(L,'Platform','Fontsize',8);
  xlabel('Minutes','fontsize',10);
  yticks([]); yticklabels([]);
  h=hline(1:numel(maze.platforms),'-');
  set(h,'Color',[0 0 0 1])
  %yticks(0:numel(maze.platforms)+1);
  %temp = {'0','1'};
  %yticklabels( repmat(temp, 1, numel(maze.platforms)) )

end

linkaxes(lnkx,'x');

% Platform spaces
if table_statespaces
    %plot_statespace_groupby(ax, T, ):
end

set(findobj(gcf,'type','axes'),'box','off');
return




  function ax = select_axis(where)
  % Selects axis locations for this function 

      switch where
      case 'upper left'   ,  loc = [1:3];
      case 'upper right'  ,  loc = [4:6];
      case 'middle left'  ,  loc = [7:9];
      case 'middle right'  , loc = [10:12]; 
      case 'lower left'  ,   loc = [13:15]; 
      case 'lower right'  ,  loc = [16:18]; 
      case 'lowest left'  ,  loc = [19:21]; 
      case 'lowest right'  , loc = [22:24]; 
        case 'all lower right', loc = [16:18, 22:24];
          case 'all lower left', loc = [13:15, 19:21];
      end

      if notstatespace
          ax = subplot(3,6, loc);
      else
          ax = subplot(4,6, loc);
      end

  end
    
    
  function deactivate()
  % function to run during onCleanup if the function has a run detector
    plotmaze_active=false;
  end

  function plot_performance_per_platform(ax)
    % plots performance levels per each type of
    % platform

    cla
    cnt = 0;
    cnts    = zeros(2*numel(maze.platforms),1);
    tracker = zeros(numel(maze.platforms),1);
    labels  = cell(size(cnts));
    for i = maze.platforms
      incorrects=sum(perf.isinrow(-i,perf.record(:)));
      corrects=sum(perf.isinrow(i,perf.record(:)));
      perct           = corrects/(corrects+incorrects);
      perf.total(i) = corrects + incorrects;
      if corrects || incorrects
        perf.percent(i) = perct;
      else
        perf.percent(i) = 0;
      end
      %set(b1,'EdgeColor',col,'FaceColor',col);
      tracker(i) = cnt;
      cnts(2*i-1) = [cnt];
      cnt   = cnt+2;
      labels{2*i-1} =   sprintf('R_%d',i);
      %labels{2*i} =     sprintf('R_%d',i);
    end
    b0 = bar(tracker,perf.percent);
    col =[0 0 0];
    set(b0,'EdgeColor',col,'FaceColor',col,'facealpha',0.5);
    hold on;
    b=bar(tracker,perf.total(:)/sum(perf.total));
    b.FaceAlpha = 0.5;
    %b.FaceColor = [255 69 0];
    % axis([-1 cnt-1 0 1]);
    legend([b0 b],'%% Corr','Trial Dist');
    if prettyplot; title(ax,'Well-by-Well % Correct'); end

    nonzeros = cnts ~= 0;
    nonzeros(1) = true;
    xticks(ax,cnts(nonzeros));
    set(ax,'xticklabel',labels(nonzeros),'fontsize',8,'TickLabelInterpreter','tex');
    if prettyplot; title(sprintf('Per Platform\nPerformance Total')); end
    end


  function plot_time(ax,offlinemode)
  % Records the time wrt to the last statescript
  % event
    cla
    ax.Tag = 'TimeText';
    if offlinemode
      now=max(perf.time/60e3);
      if ~isfield(state,'initialtime'); perf.initialtime = min(perf.time); end;
    else
      now=datetime('now');
    end
    t=text(ax,0,0.25,sprintf('\n\n\n%s\n\n Last event:\n\t%s\n',now,now-perf.initialtime),'FontSize',15,'Interpreter','tex');
    ylim=[t.Extent(2) t.Extent(2)+t.Extent(4)]; xlim=[t.Extent(1) t.Extent(1)+t.Extent(3)];
    if range(ylim)==0; ylim(1)-0; ylim(2)+1; end
    xlim = xlim + [0.1*range(xlim),0*range(xlim)];
    ylim = ylim + [0.1*range(ylim),-0.1*range(ylim)];
    ylim=ylim-[range(ylim)*0.1,0];xlim=xlim-[range(xlim)*0.5,0];
%     xlim=get(gca,'xlim');
%     xlim=xlim-[10,0];
%     set(gca,'ylim',ylim,'xlim',xlim);
    box off;
    ax.Visible='off';
    set(ax,'ylim',ylim,'xlim',xlim);
  end

  function plot_correrr_sumamry(ax); try
    % Plots the total correct and error trials


    cla(ax)

    b=[];
    b(1)=bar(3,perf.incorrect);hold on;
    b(2)=bar(4.25,perf.correct);
   % text(3.1,max(perf.incorrect,perf.correct)*1.15,sprintf('\n\nCurrent performance=\n%2.1f%%',100*perf.correct/(perf.incorrect+perf.correct)),'fontweight','bold','color','white','fontsize',10.5);
    text(3.1,max(perf.incorrect,perf.correct)*1.15,sprintf('\n\nCurrent performance=\n%2.1f%%',100*perf.correct/(perf.incorrect+perf.correct)),'fontweight','bold');
    col =[0 0 0];
    set(b(1:2),'EdgeColor',col,'FaceColor',col);
    set(b(1),'facealpha',0.5);
    if prettyplot; title(sprintf('Performance Total\n')); end
    ylim=[0,max(perf.incorrect,perf.correct)+5];
    set(ax,'xtick',[3,4.25],'xticklabel',{'Incorrect','Correct'},'fontsize',8,'ylim',ylim.*[1 1.2]);

    % Types of incorrect trial (Incorrect poke versus incorrect abortions)
%     ax=subplot(3,6,2);
%     col =[0 0 0];
%     b(1)=bar(0,perf.end.abort);hold on;
%     b(2)=bar(1,perf.incorrect-perf.end.abort);
%     set(ax,'xtick',[0,1],'xticklabel',{'Abort\newlineIncorrect','Poke\newlineIncorrect'},'fontsize',8,'ylim',ylim.*[1 1.2]);
%     set(b(1:2),'EdgeColor',col,'FaceColor',col);
%     set(b(1:2),'facealpha',0.5);
%     title('Types Incorrect');
%
%    % Types of trials (those poked versus aborted)
%     ax=subplot(3,6,3);
%     b0=bar(0,perf.end.poke);hold on;
%     b1=bar(1,perf.end.abort);
%     col =[0 1 1];
%     set(b0,'EdgeColor',col,'FaceColor',col,'FaceAlpha',0.5);
%     col=[1 0 1];
%     set(b1,'EdgeColor',col,'FaceColor',col,'FaceAlpha',0.5);
%     title('Trial Attemps\newline /Non-Atempts','Fontsize',8);
%     set(ax,'xtick',[0,1],'xticklabel',{'Poke','Aborts'},'Fontsize',8);
  end, end

  function p = plot_statespace_groupby(ax, T, varargin)
  % Much later developed feature where I record performance into a table
  %
  %  This function can take that table and group by certain characteristics
  %  and plot the statespace via those grouped features. Much more flexible
  %  for the future development of new features
  %
  % Parse optional args
  infer_bp = true;
  record = 'correct';
  group = ones(1,size(T,1));
  color = [];
  add_index=false;
  cmap  = 'haline';
  record_perf = false;
  for v = 1:2:numel(varargin)
      switch varargin{v}
      case 'group',   group   = varargin{v+1};
      case 'record', record = varargin{v+1};
      case 'background_prob', 
          background_prob = varargin{v+1};
          infer_bp        = false;
      case 'cmap',  cmap  = varargin{v+1};
      case 'color', color = varargin{v+1};
      case 'add_index', add_index = varargin{v+1};
      otherwise, error('Option %s not recognized', varargin{v+1});
      end
  end
  
  nanfilter = ~isnan(T.(record));
  T = T(nanfilter,:);

  % Determine grouping
  if ischar(group)
      [G, ids] = findgroups(T.(group));
  else
      G = group(nanfilter);
      ids = unique(G);
  end

  % Determine background probability
  if infer_bp
      background_prob = 1/numel(ids);
  end

  

  % Set color sequence
  if ~isempty(color)
      cset = color;
  else
      cset = cmocean(cmap, numel(ids)+2);
      cset = cset(2:end-1,:);
  end

  clear p;
  p=gobjects(1,numel(ids));
  for i = 1:numel(ids)

      Tsub = T(G == i,:); 
      
      if size(Tsub,1) > 4
        try
          throw_out = isnan(Tsub.(record));
          P = ry_getestprobcorrect(Tsub.(record)(~throw_out), background_prob, 0);
          if add_index
            P = P+(i-1);
          end
          PTime = Tsub.time(~throw_out)/60e3;
          CIt = [PTime(1:end)', PTime(end:-1:1)'];
          CI  = [P(2:end,2), P(end:-1:2,3)];
        catch ME
          P = nan(size(Tsub.time) + [1 0]);
          PTime = nan(size(P)-[1 0]);
          CIt = [nan];
          CI  = [nan];
        end
        p(i)=plot(ax,PTime, P(2:end,1),'o-','linewidth', 2, 'markersize', 2, 'Color', cset(i, :)); hold on;

        if any(~isnan(CI))
          pat=patch(ax,CIt(:),CI(:), 'k', 'facealpha', 0.20, 'edgealpha', 0, 'edgecolor', 'k', 'facecolor', cset(i,:));
        end
        set(gca,'yscale','linear');
        
        if add_index
          l=line(ax,[PTime(1) PTime(end)], [background_prob+i-1  background_prob+i-1 ],...
            'linewidth',2,'linestyle','--');
          l.Color = cset(i,:);
        end

        if record_perf
            %perf.table(Ptime(end) == perf.table.time, 'wellstatespace') = P(end,1);
        end
        
      end

  end
  
  if add_index && numel(ids)>0
    set(gca,'Ylim',[-0.25 numel(ids)]);
  elseif size(T,1)>1
    l=line(ax,[T.time(1) T.time(end)]/60e3, [background_prob  background_prob ],...
      'linewidth',2,'linestyle','--');
    if numel(ids)==1
        l.Color = cset(1,:);
    else
        l.Color = 'k';
    end
  end
  
  if size(T,1)>1
    %set(gca,'XLim',[T.time(1),T.time(end)]/60e3);
  end
    
  end

  function plot_statespace(ax), try
  % Function that takes performance records and will output the state
  % space representation of the animal's behavior during the task.
    tic
    cla(ax, 'reset');

    % OVERALL SPACE!
    background_prob = 0.25;

    performance = perf.record>0;
    performance(isnan(perf.record)) = [];
    perftime = perf.time(~isnan(perf.record))/60e3;

    P = ry_getestprobcorrect(performance(:), background_prob, 0);

    CIt = [perftime(1:end)', perftime(end:-1:1)'];
    CI = [P(2:end,2), P(end:-1:2,3)];

    hold off;
    patch(ax,CIt(:),CI(:), 'b','facealpha',0.175,'edgealpha',0);
    hold on;
    plot(ax,perftime', P(2:end,1),'b-','linewidth',2);
    box(ax,'off');
    axis(ax,[perftime(1) perftime(end) 0 1.05]);
    line([perftime(1) perftime(end)], [background_prob  background_prob ],...
       'linewidth',2,'linestyle',':'); % this might be performance instead?
    xlabel('Minutes','fontsize',10)
    ylabel('Probability of a\newline Correct Response','fontsize',10)
    if prettyplot; title('Statespace','fontsize',10); end
    
    if exist('const','var')
      background_prob     = 1/2;%1/4 * 1/4;
    else
      background_prob     = 1/2;%1/4 * 1/4;
    end

    % Seqeuence space : how well animal is completing sequences
    if isfield(perf,'srecord') %&& background_prob<0.25
      tic
      performance = perf.srecord>0;
      %performance(isnan(perf.srecord)) = [];
      %perftime = perf.stime(~isnan(perf.srecord))/1e3;
      perftime = perf.stime/60e3;
      P = ry_getestprobcorrect(performance(:), background_prob, 0);

      CIt = [perftime(1:end)', perftime(end:-1:1)'];
      CI  = [P(2:end,2), P(end:-1:2,3)];

      plot(ax,perftime', P(2:end,1),'g-','linewidth',2);
      p=patch(ax,CIt(:),CI(:), 'g','facealpha',0.15,'edgealpha',0);
      p.FaceColor='g';
      line([perftime(1) perftime(end)], [background_prob  background_prob ],'linewidth',2,'linestyle','--','color','g');

      set(gca,'yscale','linear');
      toc
    end

    % Working memory space: how well animal is completing working memory trials
    if isfield(perf,'wrecord') %&& background_prob<0.25
      tic
      performance = perf.wrecord>0;
      %performance(isnan(perf.srecord)) = [];
      %perftime = perf.stime(~isnan(perf.srecord))/1e3;
      perftime = perf.wtime/60e3;
      P = ry_getestprobcorrect(performance(:), background_prob, 0);

      CIt = [perftime(1:end)', perftime(end:-1:1)'];
      CI = [P(2:end,2), P(end:-1:2,3)];
      plot(ax,perftime', P(2:end,1),'r-','linewidth',2);
      p=patch(ax,CIt(:),CI(:), 'r','facealpha',0.15,'edgealpha',0);
      p.FaceColor='r';
      line([perftime(1) perftime(end)], [background_prob  background_prob ],'linewidth',2,'linestyle','--','color','g');
      set(gca,'yscale','linear');
      toc
    end
    toc

    catch ME
      warning('errored out in plotmaze');
      printstruct(ME.stack)
    end
  end

  function plot_cumulative_statistics(ax)
  % Plots cumulative statistics about the behavior
    cla(ax);
    if isfield(perf,'ctime') && isfield(perf,'btime')
      plot(perf.ctime/60e3,perf.crecord,'rs-.','MarkerFaceColor',[1 0 0]);
      hold on;
      plot(perf.btime/60e3,perf.brecord,'bo-.');
      %xlim = [min([perf.ctime, perf.btime]) max([perf.ctime,perf.btime])]/1e3;
      xlim=get(gca,'xlim');
      ylim = [min([perf.crecord, perf.brecord]) max([perf.crecord,perf.brecord])];
      text( xlim(1)+range(xlim)*0.05, ylim(2)*0.9 , sprintf('blocklockout\ncurrent=%d\nworst=%d',state.bcount,max(perf.brecord)),'fontweight','bold');
      text( xlim(2)-range(xlim)*0.225 , ylim(2)*0.9 , sprintf('run\ncurrent=%d\nbest=%d',state.rcount,max(perf.crecord)),'fontweight','bold');
      try; set(gca,'ylim',ylim+[-range(ylim)*0.1,range(ylim)*0.1]); catch ME; end
          if prettyplot; title('Runs and Cumulative Perf','fontsize',10); end
    else
      set(ax,'visible','off');
    end
end


  function plot_rewarderror_dynamics(ax)
  % Records the actual reward and error events versus time, colored green
  % and red respectively. The yaxis is the well that was poked. Pokes that
  % did not result in a reward error evaluation are symbolized by a plain
  % dot. A right yaxis plots the cue duration if a working memory event
  % is turned on.
    cla(ax);
    yyaxis(ax,'left');
    s=stairs(perf.tr(perf.time)/60e3,abs(perf.tr(perf.record)),'o-');
    s.MarkerFaceColor=s.Color;
    hold on;
    ylabel(ax, 'Platform\newline Poked','fontsize',10);
    set(ax,'ylim',[ax.YLim(1) ax.YLim(2)+1]);

    % Place patch objects that mark correct and incorrect regions.
    x = perf.tr(perf.time)/60e3;
    y = perf.tr(perf.record > 0);
    x=x(y);
    if ~isempty(x)
        x1 = [x; x+0.02; x-0.02; x-0.021; x+0.021];
        %x1 = [x-1.001; x-1; x-1.001; x; x+1; x+1.001];
        ya=ax.YLim(2)*ones(numel(x)*3,1);
        yb=ax.YLim(2)*zeros(numel(x)*2,1);
        y1 = [ya;yb];
        Z=[x1 , y1];
        Z=sortrows(Z,1);
        Z = [Z; min(x), 0];
        p1=patch(Z(:,1),Z(:,2),'g');
        set(p1,'facealpha',0.2,'edgealpha',0.2);
    end
    x = perf.tr(perf.time)/60e3;
    y = perf.tr(perf.record < 0);
    x=x(y);
    if ~isempty(x)
        x1 = [x;x+0.02;x-0.02;x-0.021;x+0.021];
        ya=ax.YLim(2)*ones(numel(x)*3,1);
        yb=ax.YLim(2)*zeros(numel(x)*2,1);
        Z=[x1 , [ya;yb]];
        Z=sortrows(Z,1);
        p1=patch(Z(:,1),Z(:,2),'r');
        set(p1,'facealpha',0.2,'edgealpha',0.2);
    end
    if isfield(perf,'ptime')
      s=stairs(perf.ptime/60e3,perf.precord,'k.','linewidth',1);
      s.Color=[s.Color 0.5];
    end
    % set(get(gca,'YLabel'),'String','Correct Incorrect');
    set(ax,'XLim',[max([0 min(perf.time(~isnan(perf.time)))])/60e3 inf],'YLim',[0 max(maze.platforms)+1.00001],'ytick',maze.platforms);
    if prettyplot; title(ax,'Poke Record + Adaption Variables','fontsize',10); end
    if isfield(perf,'adapt') && numel(perf.adapt.time)>1
      yyaxis(ax,'right');
      try
      plot(perf.adapt.time(:)/60e3,perf.adapt.wm(:),'--');
      set(ax,'XLim',[max([0 min(perf.time(:))])/60e3-5 inf],'YLim',[-inf inf]);
      catch ME
        ME
      end
      ylabel('Cue Duration','fontsize',10);
    end

  end

end
    
