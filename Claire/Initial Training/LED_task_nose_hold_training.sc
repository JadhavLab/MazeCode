%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%%  Name:         LED-only Task
%%
%%  Purpose:      After nose poke, presents LEDs near either reward well, signaling which one will dispense reward
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
int RightRewardWell = 2
% ---------------------
% Digital Output Assigments
% ----------------------
int left_pump =  3
int right_pump = 4
int nose_poke_led = 5
int left_led = 1
int right_led = 2


% ---------------------
% LED to Path Variables
% ----------------------
int LEFT_PATH_LED = 1
int RIGHT_PATH_LED = 1


% ---------------------
% Reward Parameters
% ---------------------
int reward_time = 500 % how long milk is delivered
int time_until_reset = 100 %this is the inter-trial interval


% ---------------------
% Behavior Trackers
% ---------------------
int sampled_well = 1 %has a reward well been sampled or is this the start of a training session
int last_sampled_smell = 0 % time when a smell was sampled last, used for checking if ITI has passed
int current_time = 0 %time check for ITI comparison
int time_diff = 0 %time that has passed since last poke and current poke used for ITI
int last_sampled_smell = 0
int nose_hold_time = 10 % how long the animal must poke before odor is delivered
int nose_hold_start = 0 %time that animal started to poke
int clock_update = 0  %variable that updates for the do every loop to track nose held time
int time_held = 0 %variable that has actual nose held time in increments of clock_update
int exit_condition = 0 % tracks whether the animal has removed its nose from the nose poke
int poke_void_tracker = 0 		% 0 - not in an error state, 1 in an error state
int consecutive_error_tracker = 0
int correct_trial_counter = 0
int left_led_on = 0
int right_led_on = 0
int start_of_block = 1

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
	if correct_trial_counter < 30 do
		portout[left_led] = 1 %turn on left LED
		left_led_on = 1
		portout[left_pump] = 1  % Administer reward
		do in reward_time
			portout[left_pump] = 0
	end					
	else do
		portout[right_led] = 1
		right_led_on = 1
		portout[right_pump] = 1 % Adminster reward
		do in reward_time
			portout[right_pump] = 0
	end
	end
	if correct_trial_counter > 39 do
	correct_trial_counter = 0
	end

end;
	

%%%%%%%%%%%%%%%%%%%%%%%%%
%%   CALLBACK SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%

%% START OF TRIAL
%Will automatically turn on nose poke LED at the start of the trial


		
		
		
%% DETECTION OF POKE
% When a poke is detected, one of the LEDs will turn on
callback portin[5] up %when portin1 is in up state
	disp('Nose Poke!')
	exit_condition = 0
	current_time = clock()
	nose_hold_start = current_time
	while exit_condition == 0 do every 10  % this loop checks every 10ms for how long nose has been in the nose poke
		clock_update = clock()
		time_held = clock_update - nose_hold_start
		disp(time_held)
		if  time_held  >= nose_hold_time do
			portout[nose_poke_led] = 0	%turn off nose poke LED
			disp('nosepoke off')
			trigger(1)
			sampled_well = 0	
    		end
	end
	% we are not in an error state anytime after request for sample
	poke_void_tracker = 0
end;



callback portin[5] down
	exit_condition = 1
	nose_hold_start = 0
	clock_update = 0
end;

%% DOES ANIMAL CORRECTLY ASSOCIATE?

% LEFT ARM Callback
callback portin[1] up   %animal pokes in left reward well
	disp('Poke Left Well')
	

	if ( left_led_on == LEFT_PATH_LED ) && (poke_void_tracker != 1) do 

		% Describe what's about to happen for matlab callback functions
		
		disp('Left Well Rewarded')
		correct_trial_counter = correct_trial_counter + 1
					disp('correct trials =')
					disp(correct_trial_counter)
		portout[left_led] = 0 %turn off left LED
		portout[right_led] = 0 %turn off right LED
		portout[nose_poke_led] = 1 %turn on nose poke LED
		disp('nosepoke on')
		left_led_on = 0

	else do
		disp('Not rewarded')
		consecutive_error_tracker = consecutive_error_tracker + 1
		left_led_on = 0
		portout[left_led] = 0 %turn off left LED
		portout[right_led] = 0 %turn off right LED
		portout[nose_poke_led] = 1 %turn on nose poke LED
		disp('nosepoke on')
	end
	
	
	sampled_well = 1


	poke_void_tracker = 1

end;

callback portin[1] down
	disp('Left well down')
end;

% RIGHT ARM Callback
callback portin[2] up
	disp('Poke Right Well')

	
	if (right_led_on == RIGHT_PATH_LED) && (poke_void_tracker != 1) do
		
		% Describe what's about to happen for matlab callback functions
		
		disp('Right Well Rewarded')
		correct_trial_counter = correct_trial_counter + 1
					disp('correct trials =')
					disp(correct_trial_counter)
		
	else do 
		disp('Not rewarded')	
		consecutive_error_tracker	= consecutive_error_tracker + 1
		right_led_on = 0
		
	end
	portout[right_led] = 0 %turn off right LED
	portout[left_led] = 0 %turn off left LED
	portout[nose_poke_led] = 1 %turn on nose poke LED


	poke_void_tracker = 1	
	sampled_well = 1

end;

callback portin[2] down
	disp('Right Well down')
end;

