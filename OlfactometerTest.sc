clear all;

int count = 100

int delay_ext = 2000
int delay_int = 500

disp('Variables initialized');

callback portin[1] up
		disp('Initializing program')

		portout[1] = 1
		portout[2] = 1
		portout[3] = 1
		portout[4] = 1
		do in 100
			disp('100')
		end
end;

callback portin[1] down
	disp('Port 2 down')

	
	portout[1] = 0
	portout[2] = 0
	portout[3] = 0
	portout[4] = 0
end;