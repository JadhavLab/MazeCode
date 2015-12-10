%clear all;

int zero = 0
int one = 1

int delay_ext = 2000
int delay_int = 500

disp('Variables initialized');

callback portin[1] up
	disp('Port 1 up')
end

callback portin[1] down
	disp('Port 1 down')
end;

callback portin[2] down
	disp('Port 2 down')
end;

callback portin[2] up
	disp('Initializing program!')

	%while (zero < one) do every delay_ext
		%do in delay_int
			disp('Solenoids on')

			portout[1] = 1
			portout[2] = 1
			portout[3] = 1
			portout[4] = 1
			portout[5] = 1
			portout[6] = 1
		%end
				

		%do in delay_int
			%disp('Solenoids off')
			%portout[1] = 0
			%portout[2] = 0
			%portout[3] = 0
			%portout[4] = 0
			%portout[5] = 0
			%portout[6] = 0
		%end
	end
end;