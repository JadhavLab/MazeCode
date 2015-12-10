% PROGRAM NAME: 	OPTOGENETICSSTIMPROTOCOL
% AUTHOR: 			MCZ
% DESCRIPTION:          Will pulse the laser at a specified pulse frequency and pulse duration for a specified amount of time. 
%                                        Inputs mapped:  1 2 3 4 = [0.1Hz, 0.2Hz, 1Hz, 4Hz]
%                                                                      5 6 7 = [100ms, 500ms, 1000ms]
%						   Outputs mapped: 1         = [Laser]


%VARIABLES

int expDuration= 20000    % duration of experiment in ms
int totalDuration= 0           % duration of current stimulation 
int stimWidth= 0                % stimulation duration    [100ms, 500ms, 1000ms]
int stimFreq= 0                  % stimulation frequency [0.1Hz, 0.2Hz, 1Hz, 4Hz]
int isRunning= 0                % function execution lockout

%% CALLBACK VARIABLES

int variableFreq = 0
int variableWidth = 0

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% FUNCTIONS

% -----------------------
% Function Name: 	Stimulate
% Description:		This function activates the laser, using variable stimFreq and StimWidth times
% -----------------------

function 1

	% if you want the laser on longer than the on/off time, or these vars don't exist yet do
	if stimWidth >= stimFreq || stimWidth== 0 || stimFreq== 0 || isRunning==1 do
	
		if stimWidth==0 || stimFreq== 0 do
		disp('Error, stimWidth or StimFreq values are not set')
		else if isRunning==1 do
		disp('Error, function is already running')
		else do
		disp('Error, stimWidth is longer than stimFrequency')
		end

	else do
		isRunning=1
		% while time elapsed is less than specified duration, do every frequency of stimulation
		while totalDuration <= expDuration  do every stimFreq
			portout[1]= 1 % turn the laser on
			do in stimWidth % after pulse duration, turn the laser off
				portout[1]= 0
			end
			totalDuration= totalDuration + stimFreq % add up time elapsed
		then do % when stimulation is done, reset totalDuration
		disp('done!')
		totalDuration= 0
		isRunning= 0
		end

	end

end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CALLBACKS -- EVENT-DRIVEN TRIGGERS

% Button #1 - 0.1Hz Stimulation Selected
callback portin[1] up
	disp('portin1 up- 0.1Hz Stimulation Selected')
	stimFreq= 10000 % every 10sec 
	trigger(1)
end;
callback portin[1] down
	disp('portin1 down')
end;

% Button #2 - 0.2Hz Stimulation Selected
callback portin[2] up
	disp('portin2 up- 0.2Hz Stimulation Selected')
	stimFreq= 5000 % every 5sec 
	trigger(1)
end;
callback portin[2] down
	disp('portin2 down')
end;

% Button #3 - 1Hz Stimulation Selected
callback portin[3] up
	disp('portin3 up- 1Hz Stimulation Selected')
	stimFreq= 1000 % every 1sec 
	trigger(1)
end;
callback portin[3] down
	disp('portin3 down')
end;

% Button #4 - 4Hz Stimulation Selected
callback portin[4] up
	disp('portin4 up- 4Hz Stimulation Selected')
	stimFreq= 250 % every 250msec 
	trigger(1)
end;
callback portin[4] down
	disp('portin4 down')
end;

% DIP #1 - 100ms Stimulation Width Selected
callback portin[5] up
	disp('portin5 up- 100ms Stimulation Width Selected')
       stimWidth= 100  
	trigger(1)
end;
callback portin[5] down
	disp('portin5 down')
end;

% DIP #2 - 500ms Stimulation Width Selected
callback portin[6] up
	disp('portin6 up- 500ms Stimulation Width Selected')
       stimWidth= 500 
	trigger(1)
end;
callback portin[6] down
	disp('portin6 down')
end;

% DIP #3 - 1000ms Stimulation Width Selected
callback portin[7] up
	disp('portin7 up- 1000ms Stimulation Width Selected')
	 stimWidth= 1000 
	trigger(1)
end;
callback portin[7] down
	disp('portin7 down')
end;

callback portin[8] up
	disp('portin 8 down')
	disp('  ... variable stimulation ... ')
	stimFreq = variableFreq
	trigger(1)
	
end;

callback portin[8] down
end;	

callback portin[9] up
	disp('portin 7 down')
	disp('  ... variable stimulation ... ')
	stimWidth = variableWidth
	trigger(1)
	
end;

callback portin[9] down
end;	

