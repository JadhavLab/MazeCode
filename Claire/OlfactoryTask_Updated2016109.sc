%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%%  Name:         Olfactory Task
%%	Author: 	  Claire Symanski
%%  Purpose:      Animal must nose poke for adequate amount of time (indicated by tone), during which an odor will be presented, in pseudorandom order. Animal must choose the corresponding reward well.
%%				
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
int beep = 9

%----------------------
% Behavior Trackers
%----------------------
int new_trial = 1
int consecutive_left = 0
int consecutive_right = 0
int odor_picked = 0
int left_odor_dispensed = 0
int right_odor_dispensed = 0
int correct_trials = 0
int total_complete_trials = 0
int nose_held = 0
int sucessful_nose_poke = 0
int nose_hold_errors = 0
int time_out = 0

%-----------------------
% Adjustable Parameters
%-----------------------
int smell_delivery_period = 800
int time_out_period = 1000
int nose_hold_time = 500
int beep_duration = 300
int reward_time = 300


%% ZERO OUT THE CLOCK! %%
clock(reset);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% HELPER FUNCTIONS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function 1 %chooses an odor and dispenses it

if new_trial == 1 do %picks a new odor only at the beginning of each trial (does not pick a new odor if animal did not hold nose for long enough)
	if (consecutive_left > 2) || (consecutive_right > 2) do % if there have been 3 trials on the same side in a row, choose the other side
		if consecutive_left > 2 do
			odor_picked = 7
			consecutive_left = 0
			consecutive_right = 1
		end
		if consecutive_right > 2 do
			odor_picked = 6 
			consecutive_right = 0
			consecutive_left = 1
		end
	else do %otherwise pick a random side
		odor_picked = random(1) + 6
		if odor_picked == 6 do
			consecutive_right = 0
			consecutive_left = consecutive_left + 1
		end
		if odor_picked == 7 do
			consecutive_left = 0
			consecutive_right = consecutive_right + 1
		end		
	end 
	
end
portout[odor_picked] = 1 %dispense smell picked
disp('Dispensing odor')
disp(odor_picked)
	do in smell_delivery_period
		portout[odor_picked] = 0
	end
new_trial = 0

end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function 2 % activate correct reward well

if odor_picked == 6 do
	left_odor_dispensed = 1 %indicates left is correct response
end

if odor_picked == 7 do 
	right_odor_dispensed = 1 %indicates right is correct response
end

end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function 3 % time out period
if time_out == 0 do
	time_out = 1
	do in time_out_period
		time_out = 0
	end
end
end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function 4 %keeps track of how many times animal made correct choice
	correct_trials = correct_trials + 1
	total_complete_trials = total_complete_trials +1
			disp(correct_trials)
			disp(total_complete_trials)
end;


%%%%%%%%%%%%%%%%%%%%%%%%%
%%   CALLBACK SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%

%NOSE POKE TRIGGERED
callback portin[5] up
	if time_out == 0 do
		nose_held = 1
		portout[vacuum] = 0
		trigger(1) %choose a smell and dispense it
		do in nose_hold_time
			if nose_held == 1 do 
				portout[beep] = 1
				do in beep_duration
					portout[beep] = 0
				end
				trigger(2) %activate correct reward well
				trigger(3) %lockout period
				sucessful_nose_poke = 1
			else do
				nose_hold_errors = nose_hold_errors + 1
				disp(nose_hold_errors)
				sucessful_nose_poke = 0
			end
		end
	end
end;

callback portin[5] down
	nose_held = 0
	portout[vacuum] = 1
end;

%LEFT REWARD WELL TRIGGERED
callback portin[1] up
	if sucessful_nose_poke == 1 do
		if ( left_odor_dispensed == 1) do 
			trigger(4) %correct trial counter
			portout[left_pump] = 1  % Administer reward
				do in reward_time
					portout[left_pump] = 0
				end
		else do
			total_complete_trials = total_complete_trials +1
			disp(total_complete_trials)
		end
		
		left_odor_dispensed = 0
		right_odor_dispensed = 0
		
		if  sucessful_nose_poke == 1 do
			new_trial = 1
		else do 	% if nose-hold error, do not pick a new smell
			new_trial = 0
		end
		sucessful_nose_poke = 0
	end
end;


%RIGHT REWARD WELL TRIGGERED
callback portin[2] up
	if sucessful_nose_poke == 1 do
		if ( right_odor_dispensed == 1) do
			trigger(4) %correct trial counter
			portout[right_pump] = 1  % Administer reward
				do in reward_time
				portout[right_pump] = 0
					end
		else do
			total_complete_trials = total_complete_trials +1
			disp(total_complete_trials)
		end
		
		left_odor_dispensed = 0
		right_odor_dispensed = 0
			
		if  sucessful_nose_poke == 1 do
			new_trial = 1
		else do 	% if nose-hold error, do not pick a new smell
			new_trial = 0
		end
		sucessful_nose_poke = 0
	end
end;
