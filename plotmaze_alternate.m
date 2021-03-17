function f=plotmaze_alternate(perf,maze,state,offlinemode)

    if nargin < 4 ; offlinemode = false ; end

    global const
    if exist('const','var') && isfield(const,'debug') && isequal(const.debug,'plotmaze'), keyboard; end

    notstatespace = false;
    
    % TODO add getestprobcorrect output to a panel in the output! This
    % would be super helpful
    % TODO maybe to save time, think about changing the clf to selective
    % cla of the graphs that need to be changed (not sure if this will
    % interfere with anything, but hopefully not).
    tic
    try
    % First figure gears towards counting correct and incorrect responses overall
    % for the entire task and for each well.
    f=sfigure(1);b=[];
    if notstatespace ; ax=subplot(3,6,[1:3]); else ax=subplot(4,6,[1:3]);  end
    cla
    b(1)=bar(3,perf.incorrect);hold on;
    b(2)=bar(4.25,perf.correct);
    text(3.1,max(perf.incorrect,perf.correct)*1.15,sprintf('\n\nCurrent performance=\n%2.1f%%',100*perf.correct/(perf.incorrect+perf.correct)),'fontweight','bold');
    col =[0 0 0];
    set(b(1:2),'EdgeColor',col,'FaceColor',col);
    set(b(1),'facealpha',0.5);
    title(sprintf('Performance Total\n'));
    ylim=[min(perf.incorrect,perf.correct),max(perf.incorrect,perf.correct)];
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

    % Performance Per Platform
    cnt = 0;
    if notstatespace, ax=subplot(3,6,[4:6]); else ax=subplot(4,6,[4:6]); end
    cla
    cnts = zeros(2*numel(maze.platforms),1);
    labels=cell(size(cnts));
    for i = maze.platforms
      b0    = bar(cnt,   sum(perf.isinrow(-i,perf.record(:)))); hold on;
      b1    = bar(cnt+1, sum(perf.isinrow(i,perf.record(:))));
      col =[0 0 0];
      set(b0,'EdgeColor',col,'FaceColor',col,'facealpha',0.5);
      set(b1,'EdgeColor',col,'FaceColor',col);
      title(ax,'Performance Total');
      cnts(2*i-1:2*i) = [cnt,cnt+1];
      cnt   = cnt+4;
      labels{2*i-1} =   sprintf('I_%d',i);
      labels{2*i} =     sprintf('C_%d',i);
    end
    axis([-inf inf -inf inf]);
    xticks(ax,cnts);
    set(ax,'xticklabel',labels,'fontsize',8,'TickLabelInterpreter','tex');
    title(sprintf('Per Platform\nPerformance Total'));

    % Second figure gears at showing poke and reward/error dynamics
    % if sum(perf.record>1) && numel(perf.record)>5 ; keyboard; end
    if notstatespace; ax=subplot(3,6,[7:12]); else ax=subplot(4,6,[7:12]); end
    cla
    s=stairs(perf.tr(perf.time)/1e3,abs(perf.tr(perf.record)),'o-');
    s.MarkerFaceColor=s.Color;
    hold on;
    set(get(ax,'YLabel'),'String','Platform\newline Poked');
    set(ax,'ylim',[ax.YLim(1) ax.YLim(2)+1]);

    % Place patch objects that mark correct and incorrect regions.
    x = perf.tr(perf.time)/1e3;
    y = perf.tr(perf.record > 0);
    x=x(y);
    if ~isempty(x)
        x1 = [x;x+1;x-1;x-1.001;x+1.001];
        ya=ax.YLim(2)*ones(numel(x)*3,1);
        yb=ax.YLim(2)*zeros(numel(x)*2,1);
        Z=[x1 , [ya;yb]];
        Z=sortrows(Z,1);
        p1=patch(Z(:,1),Z(:,2),'g');
        set(p1,'facealpha',0.2,'edgealpha',0.2);
    end
    x = perf.tr(perf.time)/1e3;
    y = perf.tr(perf.record < 0);
    x=x(y);
    if ~isempty(x)
        x1 = [x;x+1;x-1;x-1.001;x+1.001];
        ya=ax.YLim(2)*ones(numel(x)*3,1);
        yb=ax.YLim(2)*zeros(numel(x)*2,1);
        Z=[x1 , [ya;yb]];
        Z=sortrows(Z,1);
        p1=patch(Z(:,1),Z(:,2),'r');
        set(p1,'facealpha',0.2,'edgealpha',0.2);
    end
    if isfield(perf,'ptime')
      s=stairs(perf.ptime/1e3,perf.precord,'k.','linewidth',1);
      s.Color=[s.Color 0.5];
    end
    xlabel('Time (s)');
    % set(get(gca,'YLabel'),'String','Correct Incorrect');
    set(ax,'XLim',[min(perf.time(:))/1e3-5 inf],'YLim',[0 max(maze.platforms)+1.00001],'ytick',maze.platforms);
    title(ax,'Poke Record + Adaption Variables','fontsize',10);
    if isfield(perf,'adapt') && numel(perf.adapt.time)>1
      yyaxis(ax,'right');
      plot(perf.adapt.time(:)/1e3,perf.adapt.cue/1e3,'--');
      ylabel('Cue Duration');
    end

    %Performance stats
    if notstatespace, ax=subplot(3,6,19:24);cla(ax); else ax=subplot(4,6,13:15);cla(ax); end
    if isfield(perf,'ctime') && isfield(perf,'btime')
      plot(perf.ctime/1e3,perf.crecord,'r--.');
      hold on;
      plot(perf.btime/1e3,perf.brecord,'b--.');
      %xlim = [min([perf.ctime, perf.btime]) max([perf.ctime,perf.btime])]/1e3;
      xlim=get(gca,'xlim');
      ylim = [min([perf.crecord, perf.brecord]) max([perf.crecord,perf.brecord])];
      text( xlim(1)+range(xlim)*0.05, ylim(2)*0.9 , sprintf('blocklockout\ncurrent=%d\nworst=%d',state.bcount,max(perf.brecord)),'fontweight','bold');
      text( xlim(2)-range(xlim)*0.225 , ylim(2)*0.9 , sprintf('run\ncurrent=%d\nbest=%d',state.rcount,max(perf.crecord)),'fontweight','bold');
      try; set(gca,'ylim',ylim+[-range(ylim)*0.1,range(ylim)*0.1]); catch ME; end
      title('Runs and Cumulative Perf','fontsize',10);
    else
      set(ax,'visible','off');
    end
    
    if ~notstatespace && exist('state','var') && (offlinemode==true || state.blocklockout==false)
      tic
      ax=subplot(4,6,13:18);cla(ax);
      cla(ax)
      background_prob = 0.25;
      performance = perf.record>0;
      performance(isnan(perf.record)) = [];
      perftime = perf.time(~isnan(perf.record))/1e3;
      P = ry_getestprobcorrect(performance(:), background_prob, 0);

      CIt = [perftime(1:end)', perftime(end:-1:1)'];
      CI = [P(2:end,2), P(end:-1:2,3)];
      patch(ax,CIt(:),CI(:), 'k','facealpha',0.2);
      hold on;
      plot(ax,perftime', P(2:end,1),'r-','linewidth',2);
      
%       hold on; [y, x] = find(performance > 0);
%       h = plot(x,y+0.05,'s'); set(h, 'MarkerFaceColor','k');
%       set(h, 'MarkerEdgeColor', 'k');
%       hold on; [y, x] = find(performance == 0);
%       h = plot(x,y+0.05,'s'); set(h, 'MarkerFaceColor', [0.75 0.75 0.75]);
%       set(h, 'MarkerEdgeColor', 'k');
      axis(ax,[perftime(1) perftime(end) 0 1.05]);
      line([perftime(1) perftime(end)], [background_prob  background_prob ],'linewidth',2,'linestyle',':'); % this might be performance instead?
      %title(['IO(0.95) Learning Trial = ' num2str(lt) ' RW variance = ' num2str(sige^2) ]);
      xlabel('Time (s)','fontsize',10)
      ylabel('Probability of a\newline Correct Response','fontsize',10)
      title('Statespace','fontsize',10);
      
      if exist('const','var')
          background_prob = 1/4 * 1/3;
      else
        background_prob = 1/4 * 1/3;
      end
%       if isfield(perf,'srecord') && background_prob<0.25
%         performance = perf.srecord>0;
%         %performance(isnan(perf.srecord)) = [];
%         %perftime = perf.stime(~isnan(perf.srecord))/1e3;
%         perftime = perf.stime/1e3;
%         P = ry_getestprobcorrect(performance(:), background_prob, 0);
% 
%         CIt = [perftime(1:end)', perftime(end:-1:1)'];
%         CI = [P(2:end,2), P(end:-1:2,3)];
%         patch(ax,CIt(:),CI(:), 'k','facealpha',0.20);
%         plot(ax,perftime', P(2:end,1),'g-','linewidth',2);
%       end
      
      toc
    end
    catch ME
      ME
      ME.stack.line
    end
    
    
end