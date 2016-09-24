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
int sample_arm = 5
int left_arm = 1
int right_arm  = 2
% ---------------------
% Digital Output Assigments
% ----------------------
int left_reward =  3
int right_reward = 4
int left_odor = 6
int right_odor = 7
int nose_poke_led = 5
int left_led = 1
int right_led = 2

int odor_delivery_time = 1000


callback portin[5] up
	portout[left_odor] = 1
	disp('Dispensing left odor')
		do in odor_delivery_time
		portout[left_odor] = 0
		end
	do in 2000
		portout[right_odor] = 1
		disp('Dispensing right odor')
		do in odor_delivery_time
			portout[left_odor] = 0
		end
	end
end


