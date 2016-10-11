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
int left_solenoid = 6

%-----------------------
% Behavior Trackers
%-----------------------
int time_out = 0
int correct_trials = 0
int nose_hold_errors = 0
int successful_nose_poke_counter = 0
int backward_steps = 0
int unsuccessful_nose_poke_counter = 0
int nose_poke_attempted = 0
int nose_held = 0
int successful_nose_poke = 0

%-----------------------
% Adjustable Parameters
%-----------------------
int smell_delivery_period = 0
int time_out_period = 1000
int nose_hold_time = 100
int beep_duration = 300
int reward_time = 500


clock(reset);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% HELPER FUNCTIONS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function 1 %chooses an odor and dispenses it

portout[left_solenoid] = 1 %dispense odor, should just be clean air or neutral odor for nose poke training
smell_delivery_period = nose_hold_time
	do in smell_delivery_period
		portout[left_solenoid] = 0
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

function 4 %keeps track of how many times animal successfully nose poked, and increases nose hold time
correct_trials = correct_trials + 1
		disp(correct_trials)
		disp(nose_hold_errors)
		disp(nose_hold_time)

successful_nose_poke_counter = successful_nose_poke_counter + 1
	if successful_nose_poke_counter > 3 do %only steps up after 5 consecutive successful nose poke attempts
		nose_hold_time = nose_hold_time + 100 %increases nose hold time by 100 ms
		successful_nose_poke_counter = 0
		backward_steps = 0
	end
unsuccessful_nose_poke_counter = 0
end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function 5 %keeps track of how many times animal unsuccessfully nose poked in a row, and reduces nose hold time

nose_hold_errors = nose_hold_errors + 1
		disp(nose_hold_errors)
		
unsuccessful_nose_poke_counter = unsuccessful_nose_poke_counter + 1
	if unsuccessful_nose_poke_counter > 4 && backward_steps < 2 do %only steps backward after 5 consecutive unsuccessful nose poke attempts, and hasn't stepped back more than twice already
		nose_hold_time = nose_hold_time - 50 %backward step, reduces nose hold time by 50 ms
		unsuccessful_nose_poke_counter = 0
		backward_steps = backward_steps + 1 
	end
successful_nose_poke_counter = 0
end;



%NOSE POKE TRIGGERED
callback portin[5] up
	if time_out == 0 do
		nose_poke_attempted = 1
		nose_held = 1
		portout[vacuum] = 0
		trigger(1) %dispense "odor"
		do in nose_hold_time
			if nose_held == 1 do 
				portout[beep] = 1
				do in beep_duration
					portout[beep] = 0
				end
				trigger(3) %lockout period
				successful_nose_poke = 1
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
if nose_poke_attempted == 1 do
	if successful_nose_poke == 1 do
		trigger(4) %correct trial counter
		portout[left_pump] = 1  % Administer reward
			do in reward_time
				portout[left_pump] = 0
			end
		successful_nose_poke = 0
	else do
		trigger(5)
	end
	nose_poke_attempted = 0
end
end;
