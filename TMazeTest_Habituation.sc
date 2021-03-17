% PROGRAM NAME: 	T MAZE HABITUATION
% AUTHOR: 			MD
% DESCRIPTION:  
% Habituate the animal to the T maze by delivering reward at either of the reward wells interleaved with home port visit.

%CONSTANTS

int deliverPeriod= 500   % how long to deliver the reward
%int delayPeriod = 3000  % 3 sec delay, can be changed

%VARIABLES

%int count= 0                % blink count
int lastWell= 3             % last well, 0 home, 1 left, 2 right
%int currWell= 0            % current well
int rewardWell= 0       % reward well, 1 and 2 during test
%int nowRewarding = 0 % keep track of reward being dispensed

int phase = 0  % 0 sampling phase, 1 choice phase
int nextSide = 0  % 0 (left) and 1 (right) indicating which side is open for the next sampling phase 
int lastSide = 2 
int sameSideCount = 1 % not the same side of forced choice for over 3 consecutive trials

% Digital input channels
int well1 = 1 % left
int well2 = 2 % right
int home = 8

% Digital output channels 
int homedoor = 17
int door1 = 18
int door2 = 19

int pump1= 5
int pump2= 6
int rewardPump = 0

updates off 15
updates off 16
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% FUNCTIONS

% -----------------------
% Function Name: 	Reward
% Description:		This function administers reward to a marked well.
% -----------------------

function 1
		portout[rewardPump]=1 % reward
		do in deliverPeriod % NOTE: USING A VARIABLE HERE WORKS
			portout[rewardPump]=0 % reset reward
		end
end;


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CALLBACKS -- EVENT-DRIVEN TRIGGERS

callback portin[1] up
	disp('portin1 up')
      if (lastWell == 0) do 
%         if (rewardWell == 1) do
            disp('Poke 1 rewarded')
		rewardPump = pump1 % dispense reward from here
		trigger(1)% trigger reward

%         else do 
%                disp('Poke 1 wrong choice')
%         end
      else do
             disp('Poke 1 not rewarded')
      end
%      if (rewardWell == 1 && lastWell == 0) do
%		disp('Poke 1 rewarded')
%		rewardPump = pump1 % dispense reward from here
%		trigger(1)% trigger reward
%	else do
%		disp('Poke 1 not rewarded')
%	end
%      if (phase == 0 && lastWell == 0) do % during free choice but phase already updated
%            portout[door2] = 1
%      end
end;

callback portin[1] down
	disp('portin1 down')
	lastWell=1 % well left, now last well
end;


callback portin[2] up
	disp('portin2 up')
      if (lastWell == 0) do 
%         if (rewardWell == 2) do
            disp('Poke 2 rewarded')
		rewardPump = pump2 % dispense reward from here
		trigger(1)% trigger reward

%         else do 
%                disp('Poke 2 wrong choice')
%         end
      else do
             disp('Poke 2 not rewarded')
      end

%      if (rewardWell == 2 && lastWell == 0) do
%		disp('Poke 2 rewarded')
%		rewardPump = pump2 % dispense reward from here
%		trigger(1)% trigger reward
%	else do
%		disp('Poke 2 not rewarded')
%	end
%      if (phase == 0 && lastWell == 0) do
%            portout[door1] = 1
%      end
end;

callback portin[2] down
	disp('portin2 down')
	lastWell=2 % well right, now last well
end;


		
callback portin[8] up

       disp('Home port arrival')

%      if ( lastWell != 0 ) do 
%            disp('Trial end')
%            if ( delayPeriod != 0 ) do in 200  % delay of triggering home port door
%                 portout[homedoor] = 1
            
%            do in delayPeriod
%                 portout[homedoor] = 0
%                 disp('Trial start')
%            end
        

%            end
%      end

end;
                    
callback portin[8] down
        disp('Home port departure')
        lastWell = 0
end;
