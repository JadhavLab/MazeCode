

int sol1 = 3
int sol2 = 4
int clock_interval = 1000;
int half_interval = 500;

callback portin[1] up

	disp('portin 1 up')

	while sol2 > sol1 do every 1000
			
			disp('on')
			portout[sol2] = 1
			portout[sol1] = 1

		do in half_interval
			disp('off')	
			portout[sol1] = 0
			portout[sol2] = 0
		end

	end
end;

callback portin[1] down
	disp('Down!')
end;