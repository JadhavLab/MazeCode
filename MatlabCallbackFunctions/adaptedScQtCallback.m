function adaptedScQtCallback(newLine)

%Required global variables
global scQtHistory; %multipurpose place to store processed event history
global scQtControllerOutput; %the text output from the microcontroller 
global scQtCallBackHandle; %the handle to the function called for every new event

%this is the custom callback function.  When events occur, addScQtEvent will
%call this function. 
%newLine is the last text string sent from the microcontroller

%this function keeps a record of the animal's choices and rewad history,
%and tells the microcontrooler which port to arm with reward for the next
%trial.

%the history is stored in the global variable scQtHistory for future use,
%and it is a 2-column matrix [choice reward].  In this example, the code
%assumes that choice is either 1 or 3 (2 is the initiation poke).  Reward
%is either 0 or 1.

%custom globals for this task
global scTrialcount;

if (nargin == 0)
    %Run initiation code here then return


    scTrialcount = 0;

    return;
end


%Custom parsing of the line...
atRewardPort = 0;
%now we update the action and reward history
if (~isempty(strfind(newLine,'Poke')))
    
    currentPort = str2num(newLine(strfind(newLine,'Poke')+5));
   
    if (~isempty(strfind(newLine,'not rewarded')))
        atRewardPort = 1;
        scQtHistory = [scQtHistory; [currentPort 0]];  %a 0 means no reward
    elseif (~isempty(strfind(newLine,'rewarded')))
        atRewardPort = 1;
        scQtHistory = [scQtHistory; [currentPort 1]];  %a 1 means reward
    end   
    
    if (atRewardPort == 1)
        
        %% Update relevant global variables
        
        % count the number of trials sisnce the last switch
        scTrialcount = scTrialcount+1;
        
        % produce total number of rewarded and unrewarded well events
        reward_trace = cumsum(scQtHistory(:,2) == 1);
        no_reward_trace = cumsum(scQtHistory(:,2) == 0);
        
        % potentially print debug relate messages
        % scQtHistory
        % scQtHistory(:,2) == 0
        
        
        %% Figure: Generate cumulative reward record.
        
        figure(1); hold off;
        plot(reward_trace/scTrialcount ,'b:o'); hold on;
        plot(no_reward_trace/scTrialcount ,'r:s');
        
        a =gca;
        mess1_height = 2*(a.YLim(2) - a.YLim(1))/5;
        mess2_height = 3*(a.YLim(2) - a.YLim(1))/5;
        mess3_height = 4*(a.YLim(2) - a.YLim(1))/5;
        text(1,mess2_height,sprintf('Rewarded = %d', reward_trace(end)));
        text(1,mess1_height,sprintf('Not Rewarded = %d', no_reward_trace(end)));
%         text(1,mess3_height,sprintf('Total = %d', scQtHistory));
        
        axis([-inf inf 0 1]);
        xlabel('Trial Number'); ylabel('Proportion in Type');
        title('Cumulative Record');
        
        %% Figure: Generate Well Specific Cumulative Reward
        
        % acquire well-specific information
        targetVsReward_mat = getPortStateVector(scQtHistory);
        
        figure(2); hold off;
        
        wells = targetVsReward_mat(1:size(targetVsReward_mat,2));
        cum_well_rewards = cumsum(targetVsReward_mat,1);
        
        % generate bar graph of rewards
        bar(wells, cum_well_rewards(end,:));
        title('Well Specific Reward Count');
        xlabel('Wells');
        ylabel('Reward #');
        
%         % generate history graph of rewards
%         figure(3); hold off;
%         
%         plot(wells, cum_well_rewards);
%         title('Cumulative Record Per Well');
        
        
        
        %% Figure: Generate Trajectory Specific Cumulative Reward
        
        
%         if (scTrialcount >= 2)
%              %block switch
%             scTrialcount = 0;
%            
%             tmp1 = num2str(round(rand*99));
%             tmp2 = num2str(round(rand*99));
% 
%             %Now we send control messages back to change variables in the
%             %stateScript environment
%             sendScQtControlMessage(['rewardprob1 = ',tmp1]);
%             sendScQtControlMessage(['rewardprob3 = ',tmp2]);
%             sendScQtControlMessage('disp(rewardprob1)');
%             sendScQtControlMessage('disp(rewardprob3)');
%         end
        
       
                    
    end
end

%% HELPER FUNCTIONS

    function targetVsReward_mat = getPortStateVector(historyMatrix)
        
        ports = historyMatrix(:,1);
        reward_contingency = historyMatrix(:,2);
        
        uniqPorts = unique(ports);
        
        targetVsReward_mat = NaN * ones(numel(reward_contingency, ...
            max(uniquePorts)));
        for p = uniqPorts
            port_loc = ports==p;
            targetVsReward_mat(:,p) = reward_contingency(port_loc);
        end
        
    end

    function trajVsReward_mat = getTrajectoryStateVector()
        % TODO
    end

    function path_mat = getPathMat(portStateVector)
        
        % Change all 1's marking a reward event in their respective column
        % to their respective port number
        ports = 1:size(portStateVector,2);
        positionVisits = portStateVector*ports';

        %% create [(initial, final); (initial final); ... ] matrix
        initial_final = zeros( numel(positionVisits) - 1, 2);
        initial_final(:,1) = positionVisits(1:end-1);
        initial_final(:,2) = positionVisits(2:end);

%         %% remove all repeats
%         repeat_locs = arrayfun(@(x) initial_final(x,1) == initial_final(x,2), ...
%             1:size(initial_final,1) );
%         initial_final(repeat_locs,:) = [];

        path_mat = initial_final;
    end

