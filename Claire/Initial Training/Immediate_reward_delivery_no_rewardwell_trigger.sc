%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%%  Name:         LED Task Immediate with Immediate Reward Delivery
%%
%%  Purpose:      Train rat to nose poke to get reward from reward well. No minimum nose-hold time 
%%				required. To start with right reward well, plug it into ethernet port    %%				assigned to left reward well. 
%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%
%%   VARIABLE SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%


% ---------------------
% Digital Input Assignments
% ----------------------
int NosePoke = 5
int LeftRewardWell = 1
int RightRewardWell  = 2
% ---------------------
% Digital Output Assigments
% ----------------------
int left_pump =  3
int right_pump = 4
int nose_poke_led = 5
int left_led = 1
int right_led = 2


% ---------------------
% Odor to Path Variables
% ----------------------
int LEFT_PATH_LED = 1
int RIGHT_PATH_LED = 1


% ---------------------
% Reward Parameters
% ---------------------
int reward_time = 500 % how long milk is delivered
int time_until_reset = 100 %this is the inter-trial interval
int time_out_period = 5000


% ---------------------
% Behavior Trackers
% ---------------------
int sampled_well = 1 %has a reward well been sampled or is this the start of a training session
int last_sampled_smell = 0 % time when a smell was sampled last, used for checking if ITI has passed
int current_time = 0 %time check for ITI comparison
int time_diff = 0 %time that has passed since last poke and current poke used for ITI

int clock_update = 0  %variable that updates for the do every loop to track nose held time

int poke_void_tracker = 0 		% 0 - not in an error state, 1 in an error state
int consecutive_error_tracker = 0
int correct_trial_counter = 0
int left_led_on = 0
int right_led_on = 0
int time_out = 0

% ---------------------
% Apparatus Tracker
% ----------------------
%int smell_picked = 0
%int smell_digital_out = 0;

%% ZERO OUT THE CLOCK! %%
clock(reset);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%   HELPER FUNCTION SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function 1
	portout[left_pump] = 1  % Administer reward
	do in reward_time
		portout[left_pump] = 0
	end		
end;



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function 2
	time_out = 1
	do in time_out_period
	time_out = 0
	portout[nose_poke_led] = 1
	end
end
%%%%%%%%%%%%%%%%%%%%%%%%%
%%   CALLBACK SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%	
		
		
%% DETECTION OF POKE
% When a poke is detected, one of the LEDs will turn on
callback portin[5] up %when portin1 is in up state
	disp('Nose Poke!')
		if time_out == 0 do
			trigger(2)
			portout[nose_poke_led] = 0	%turn off nose poke LED
			trigger(1)
			
    		end
   
end;

