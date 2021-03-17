% PROGRAM NAME: 	T MAZE TEST
% AUTHOR: 			MD
% DESCRIPTION:
% Full T maze test, containing a sampling phase and a choice phase. 

%CONSTANTS

int deliverPeriod= 500   % how long to deliver the reward
int delayPeriod = 30000  % 3 sec delay, can be changed

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
int well1 = 2 %1 % left
int well2 = 3 %2 % right
int home = 8

% Digital output channels 
int homedoor = 30%17
int door1 = 31 %18
int door2 = 32 %19

int pump1 = 5 % left
int pump2 = 6 % right
int pump3 = 7 % home
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

callback portin[3] up
	disp('portin3 up')
      if (lastWell == 0) do 
         if (rewardWell == 1) do
            disp('Poke 1 rewarded')
		rewardPump = pump1 % dispense reward from here
		trigger(1)% trigger reward

         else do 
                disp('Poke 1 wrong choice')
         end
      else do
             disp('Poke 1 repeated lick')
      end
%      if (rewardWell == 1 && lastWell == 0) do
%		disp('Poke 1 rewarded')
%		rewardPump = pump1 % dispense reward from here
%		trigger(1)% trigger reward
%	else do
%		disp('Poke 1 not rewarded')
%	end
      if (phase == 0 && lastWell == 0) do % during free choice but phase already updated
            portout[door2] = 1
      end
end;

callback portin[3] down
	disp('portin3 down')
	lastWell=1 % well left, now last well
end;


callback portin[2] up
	disp('portin2 up')
      if (lastWell == 0) do 
         if (rewardWell == 2) do
            disp('Poke 2 rewarded')
		rewardPump = pump2 % dispense reward from here
		trigger(1)% trigger reward

         else do 
                disp('Poke 2 wrong choice')
         end
      else do
             disp('Poke 2 repeated lick')
      end

%      if (rewardWell == 2 && lastWell == 0) do
%		disp('Poke 2 rewarded')
%		rewardPump = pump2 % dispense reward from here
%		trigger(1)% trigger reward
%	else do
%		disp('Poke 2 not rewarded')
%	end
      if (phase == 0 && lastWell == 0) do
            portout[door1] = 1
      end
end;

callback portin[2] down
	disp('portin2 down')
	lastWell=2 % well right, now last well
end;


callback portin[8] up
       disp('portin8 up')
        if ( lastWell != 0 ) do 
            disp('Trial end')
            disp('Home poke rewarded')
            rewardPump = pump3 % dispense reward from here
	    trigger(1)% trigger reward

            if ( phase == 0 )  do     % forced choice sampling phase
                nextSide = random(1)
                phase = 1
                if (nextSide == lastSide) do 
                    sameSideCount = sameSideCount + 1
                    if (sameSideCount > 3) do
                        if ( nextSide == 1 ) do
                           nextSide = 0
                        else do
                           nextSide = 1
                        end
                        sameSideCount = 1
                    end
                else do
                    sameSideCount = 1
                end

                if (nextSide ==0 ) do
                    portout[door1] = 0
                    portout[door2] = 1
                    disp('Left side forced choice')
                    rewardWell = 1
                else do
                    portout[door1] = 1
                    portout[door2] = 0
                    disp('Right side forced choice')
                    rewardWell = 2
                end
                lastSide = nextSide
            else do            % free choice phase
                portout[door1] = 0
                portout[door2] = 0
 %               disp('Free choice')
                phase  = 0
                if (rewardWell == 1) do
                    rewardWell = 2
                    disp('Right side reward, free choice')
                else do
                    rewardWell = 1
                    disp('Left side reward, free choice')
                end
            end
            
                 if ( delayPeriod != 0 ) do % in 300  % delay of triggering home port door
                      portout[homedoor] = 1
                      do in delayPeriod
                                 portout[homedoor] = 0
                                 disp('Trial start')
                      end
                else do
                      disp('Trial start')
                 end
        end
end;
                    
callback portin[8] down
        disp('portin8 down')
        lastWell = 0
end;
