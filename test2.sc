int foo = 2;
int bar = 1;

function 1
	while (foo > bar) do every 3000 

		portout[3] = 1
		portout[4] = 1

		do in 1000
			portout[3] = 0
			portout[4] = 0
		end


	end

end;


trigger(1);
