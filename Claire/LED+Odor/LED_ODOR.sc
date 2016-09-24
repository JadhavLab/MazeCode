%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%%  Name:         LED + Odor task
%%
%%  Purpose:      After nose poke, presents both an odor and a corresponding LED. Ranomized order of
%%				presentation
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
int vacuum = 8

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


% ---------------------
% Behavior Trackers
% ---------------------
int sampled_well = 1 %has a reward well been sampled or is this the start of a training session
int last_sampled_smell = 0 % time when a smell was sampled last, used for checking if ITI has passed
int current_time = 0 %time check for ITI comparison
int time_diff = 0 %time that has passed since last poke and current poke used for ITI
int nose_hold_start = 0 %time that animal started to poke
int clock_update = 0  %variable that updates for the do every loop to track nose held time
int time_held = 0 %variable that has actual nose held time in increments of clock_update
int exit_condition = 0 % tracks whether the animal has removed its nose from the nose poke
int poke_void_tracker = 0 		% 0 - not in an error state, 1 in an error state
int consecutive_error_tracker = 0
int correct_trial_counter = 0
int time_out = 0
int nose_hold_errors = 0
int total_complete_trials = 0

int first_block = 0
int second_block = 0
int first_odor = 0
int second_odor = 0
int left_led_on = 0
int right_led_on = 0


%ADJUST THESE VARIABLES AS NEEDED
int nose_hold_time = 500 % how long the animal must poke before reward will be available
int smell_delivery_period = 800 
int vacuum_time = 3000
int time_out_period = 4000 %cannot nose poke and recieve odor for this long after initial poke
int trial_reset = 20
int block_length = 10


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

if correct_trial_counter == trial_reset do
	correct_trial_counter = 0
end

if correct_trial_counter < block_length do
	portout[first_block] = 1 %turn on LED for first block
		if first_block == 1 do
			left_led_on = 1
		else do 
			right_led_on = 1
		end
		
else do
	portout[second_block] = 1 %turn on LED for second block
		if second_block == 1 do
			left_led_on = 1
		else do 
			right_led_on = 1
		end
end
end;


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function 2

first_block = random(1) + 1
disp(first_block)
if first_block == 1 do
	second_block = 2
else do
	second_block = 1
end
disp(second_block)

end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function 3

first_odor = first_block + 5
second_odor = second_block + 5
if correct_trial_counter < block_length do	
	portout[first_odor] = 1 %dispense first odor
	disp('Dispensing first odor')
		do in smell_delivery_period
			portout[first_odor] = 0
		end
else do
	portout[second_odor] = 1 %dispense second odor
	disp('Dispensing second odor')
		do in smell_delivery_period
			portout[second_odor] = 0
		end
end	
end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function 4

time_out = 1
	disp('Time out in effect')
do in time_out_period
time_out = 0
	disp('Time out over')

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
	if first_block == 0 do %on first trial determine which LED will turn on during first block
		trigger(2)
	end
	disp('Nose Poke!')
	exit_condition = 0
	current_time = clock()
	nose_hold_start = current_time
	while exit_condition == 0 do every 50  % this loop checks every 100ms for how long nose has been in the nose poke
		clock_update = clock()
		time_held = clock_update - nose_hold_start
		disp(time_held)
		if time_held > 50 && time_out == 0 do %deliver odor after this long if time out period not in effect
			trigger(3)
		end
		if  (time_held  >= nose_hold_time) && time_out == 0 do
				trigger(4) %time out period active
				do in 1500 	%turn on vacuum in 1.5 seconds
					portout[vacuum] = 1
				end
				do in vacuum_time %turn off vacuum after certain amount of time
					portout[vacuum] = 0
				end

				if sampled_well == 1 do %if rat has not yet sampled well, turn on LED for correct well
					trigger(1)
					consecutive_error_tracker = 0 % reset for error tracking
					portout[nose_poke_led] = 0	%turn off nose poke LED
					disp('nosepoke off')
					sampled_well = 0
				end	
    		end
	

			% we are not in an error state anytime after request for sample
			poke_void_tracker = 0
	end
	time_diff = current_time - last_sampled_smell
	disp('Time difference between current sampling request and previous, see next line')
	disp(time_diff)

   
end;

callback portin[5] down
	exit_condition = 1
	nose_hold_start = 0
	clock_update = 0
	if (time_held < nose_hold_time) do
		nose_hold_errors = nose_hold_errors + 1
		disp(nose_hold_errors)
	end
end;

%% DOES ANIMAL CORRECTLY ASSOCIATE?

% LEFT ARM Callback
callback portin[1] up   %animal pokes in left reward well
	disp('Poke Left Well')
	

	if ( left_led_on == LEFT_PATH_LED ) && (poke_void_tracker != 1) do 

		% Describe what's about to happen for matlab callback functions
		
		disp('Left Well Rewarded')
		correct_trial_counter = correct_trial_counter + 1
		total_complete_trials = total_complete_trials +1
					disp(correct_trial_counter)
					disp(total_complete_trials)
					if correct_trial_counter == trial_reset do
					correct_trial_counter = 0
					end
		portout[left_pump] = 1  % Administer reward
		do in reward_time
			portout[left_pump] = 0
			left_led_on = 0
		end

	else do
		if (consecutive_error_tracker < 1) do
			disp('Not rewarded')
		end
		if sampled_well == 0 do
			total_complete_trials = total_complete_trials +1
			disp(total_complete_trials)
		end
		consecutive_error_tracker = consecutive_error_tracker + 1
		left_led_on = 0
	end
	
	portout[left_led] = 0 %turn off left LED
	portout[right_led] = 0 %turn off right LED
	portout[nose_poke_led] = 1 %turn on nose poke LED
	disp('nosepoke on')
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
		total_complete_trials = total_complete_trials +1
					disp(correct_trial_counter)
					disp(total_complete_trials)
					if correct_trial_counter == trial_reset do
					correct_trial_counter = 0
					end
		portout[right_pump] = 1 % Adminster reward
		do in reward_time
			portout[right_pump] = 0
			right_led_on = 0
		end
	else do 
		if (consecutive_error_tracker < 1) do
		disp('Not rewarded')	
		end
		if sampled_well == 0 do
			total_complete_trials = total_complete_trials +1
			disp(total_complete_trials)
		end
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

