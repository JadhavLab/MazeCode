function plotTMazePerforma18);
set(0,'DefaultAxesLineWidth', 0.5);
set(0, 'defaultFigureColor', [1,1,1]nce(newline)
set(0,'defaultAxesFontSize', );
set(0, 'defaultAxesColor', [1,1,1]);
set(0,'defaultAxesFontSize',12);
set(0,'defaultTextColor','k')
set(0,'defaultAxesXColor','k')
set(0,'defaultAxesYColor','k')
set(0,'defaultAxesBox','off');
set(0,'defaultLineLineWidth',2);
set(0, 'defaultTextFontWeight', 'bold');
set(0,'DefaultAxesFontName', 'Helvetica');
% set the tickdirs to go out - need this specific order
set(groot, 'DefaultAxesTickDir', 'out');
set(groot, 'DefaultAxesTickDirMode', 'manual');
set(groot,'DefaultAxesTickLength', [0.02 0.025]);
set(0, 'defaultFigureInvertHardcopy', 'off');


global TrialInfo GUIHandles OutcomeFig;

global trialNum rewardSide reward phase savepath filename;

if (nargin == 0) %|| contains(newline, '~~~')
  %Run initiation code here then return
  
  
  trialNum = int32(0);
  %     TrialInfo = struct;
  %     inTrial = 0;
  OutcomeFig = figure('Position', [200 100 1000 800],'name','T Maze Outcome Plot',...
    'numbertitle','off', 'MenuBar', 'none', 'Resize', 'off', 'Color', [1 1 1]);
%   PerfAxe = 
  subplot(2, 2, 2, 'Color', [1 1 1], 'YLim', [0 1]);
  %     PerfAxe.Title.String = 'Choice phase performance';
  title('Choice phase performance', 'Color', 'k');
  xlabel('Trial Number');
  ylabel('Performance (5 trial ave)');
  hold on;
  %     SampleAxe =
  subplot(2, 2, 3, 'Color', [1 1 1]);
  title('Sample phase trial time');
  xlabel('Trial Number');
  ylabel('Time (s)');
  hold on;
  %     ChoiceAxe =
  subplot(2, 2, 4, 'Color', [1 1 1]);
  title('Choice phase trial time');
  xlabel('Trial Number');
  ylabel('Time (s)');
  hold on;
  
  uicontrol('Style', 'text', 'String', 'Trial Number: ', 'FontSize', 12,'Position',[60 600 150 25], 'HorizontalAlignment', 'left', 'BackgroundColor', [1 1 1]);
  GUIHandles.trialNum = uicontrol('Style','text','string',num2str(trialNum),'FontSize', 12,'Position',[230 600 80 25], 'HorizontalAlignment', 'left', 'BackgroundColor', [1 1 1]);
  
  uicontrol('Style', 'text', 'String', 'Current phase: ','FontSize', 12,'Position',[60 560 150 25], 'HorizontalAlignment', 'left', 'BackgroundColor', [1 1 1]);
  GUIHandles.currentphase = uicontrol('Style','text','string','NA','FontSize', 12,'Position',[230 560 150 25], 'HorizontalAlignment', 'left', 'BackgroundColor', [1 1 1]);
  
  uicontrol('Style', 'text', 'String', 'Correct choice: ','FontSize', 12,'Position',[60 520 150 25], 'HorizontalAlignment', 'left', 'BackgroundColor', [1 1 1]);
  GUIHandles.choice = uicontrol('Style','text','string','NA','FontSize', 12,'Position',[230 520 80 25], 'HorizontalAlignment', 'left', 'BackgroundColor', [1 1 1]);
  %     eventTime = [];
  [filename, savepath] = uiputfile('','Choose folder and file name', 'TrialInfo.mat');
%   savepath = uigetdir('Home', 'Select folder to save trial information');

  return;
end

[eventTime, eventStr] = strtok(newline);
eventTime = str2double(eventTime);

if contains(eventStr, 'Trial start')
  trialNum = trialNum + 1;
  set(GUIHandles.trialNum, 'String', num2str(round(trialNum/2)));
  %     inTrial = 1;
  TrialInfo.startTime(trialNum) = eventTime;
  return;
end

if contains(eventStr, 'forced')
  phase = 0;
  TrialInfo.phase(trialNum+1) = phase; % sampling phase
  set(GUIHandles.currentphase, 'String', 'Sampling');
  rewardSide = strtok(eventStr);
  set(GUIHandles.choice, 'String', rewardSide);
  return;
end

if contains(eventStr, 'free')
  phase = 1;
  TrialInfo.phase(trialNum+1) = phase;
  set(GUIHandles.currentphase, 'String', 'Free choice');
  rewardSide = strtok(eventStr);
  set(GUIHandles.choice, 'String', rewardSide);
  return;
end

if contains(eventStr, '1 rewarded') || contains(eventStr, '2 rewarded')
  reward = 1;
  TrialInfo.reward(trialNum) = reward;
  TrialInfo.firstPokeTime(trialNum) = eventTime;
  if phase == 1
    UpdateOutputFigure(TrialInfo, trialNum, 'reward');
  end
  return;
end

if contains(eventStr, 'wrong choice')
  reward = 0;
  TrialInfo.reward(trialNum) = reward;
  TrialInfo.firstPokeTime(trialNum) = eventTime;
  UpdateOutputFigure(TrialInfo, trialNum, 'reward');
  return;
end

if contains(eventStr, 'Trial end') && trialNum > 0
  %     inTrial = 0;
  TrialInfo.endTime(trialNum) = eventTime;
  UpdateOutputFigure(TrialInfo, trialNum, 'time');
  return;
end

save(fullfile(savepath, filename), 'TrialInfo');
saveas(OutcomeFig, fullfile(savepath, 'OutcomeFig'), 'fig');
% saveas(OutcomeFig, fullfile(savepath, 'OutcomeFig'), 'png');

% TrialInfo.reward(trialNum) = reward;
% TrialInfo.firstPokeTime(trialNum) = eventTime;
end


function UpdateOutputFigure(TrialInfo, trialNum, varargin)
global perf trialTime;
if nargin <3
  error('Not enough input');
end
switch varargin{1}
  case 'reward'
    if trialNum < 10 % less than 5 free choices
      perf(trialNum/2) = mean(TrialInfo.reward(2:2:trialNum));
    else
      perf(trialNum/2) = mean(TrialInfo.reward((trialNum-8):2:trialNum));
    end
    subplot(2, 2, 2);
    %         hold on;
    if trialNum < 3
      plot(trialNum/2, perf(trialNum/2), 'ko--');
    else
      plot([trialNum/2-1 trialNum/2], [perf(trialNum/2-1) perf(trialNum/2)], 'ko--');
    end
    title('Choice phase performance');
  case 'time'
    trialTime(trialNum) = (TrialInfo.endTime(trialNum)-TrialInfo.startTime(trialNum))/1000; % in sec
    if trialNum > 2
      if rem(trialNum, 2) == 1
        subplot(2, 2, 3);
        plot([(trialNum-1)/2 (trialNum+1)/2], [trialTime(trialNum-2) trialTime(trialNum)], 'bo--');
      else
        subplot(2, 2, 4);
        plot([trialNum/2-1 trialNum/2], [trialTime(trialNum-2) trialTime(trialNum)], 'ro--');
      end
    elseif trialNum == 1
      subplot(2, 2, 3);
      plot((trialNum+1)/2, trialTime(trialNum), 'bo--');
    else
      subplot(2, 2, 4);
      plot(trialNum/2, trialTime(trialNum), 'ro--');
    end
end

end
