% PROGRAM NAME: 	OPTOGENETICSSTIMPROTOCOL
% AUTHOR: 			MCZ
% DESCRIPTION:          Will pulse the laser at a specified pulse frequency and pulse duration for a specified amount of time. 
%                                        Inputs mapped:  1 2 3 4 = [trigger,increase frequency, 1Hz, 10ms with ]
%                                                                      5 6 7 = [15ms,+10ms, even width]
%						   Outputs mapped: 4         = [Laser]


%VARIABLES

int expDuration= 5000    % duration of experiment in ms
int totalDuration= 0           % duration of current stimulation 
int stimWidth= 10                % stimulation duration in ms
int stimFreq= 1000                  % stimulation frequency in ms
int isRunning= 0                % function execution lockout
int Hertz = 1			%stim freq in Hz

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

	disp('stimWidth = ')
	disp(stimWidth)
	disp('stimFreq = ')
	disp(stimFreq)

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
			portout[4]= 1 % turn the laser on
			do in stimWidth % after pulse duration, turn the laser off
				portout[4]= 0
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

% Button #1 - trigger stimulation
callback portin[1] up
	disp('portin1 up- Stimulating')
	trigger(1)
end;
callback portin[1] down
	disp('portin1 down')
end;

% Button #2 - increase stimulation frequency [1,2,5,10,15,....]
callback portin[2] up
	disp('portin2 up-Stimulaion Frequency Increased to:')
	if Hertz==1 do
		Hertz = 2
		disp('2 Hz')
	else if Hertz==2 do
		Hertz = 5
		disp('5 Hz')
	else if Hertz>=5 do
		Hertz = Hertz+5
		disp('Increased by 5Hz')
	end
	stimFreq = 1000/Hertz
end;
callback portin[2] down
	disp('portin2 down')
end;

% Button #3 - 1Hz Stimulation Selected
callback portin[3] up
	disp('portin3 up- 1Hz Stimulation Selected')
	stimFreq= 1000 % every 1sec 
	Hertz = 1
end;
callback portin[3] down
	disp('portin3 down')
end;

% Button #4 - +10ms stimulation width
callback portin[4] up
	disp('portin4 up-Stimulation width increases by 10ms')
	stimWidth= 10+stimWidth
end;
callback portin[4] down
	disp('portin4 down')
end;

% DIP #1 - 15ms Stimulation Width Selected
callback portin[5] up
	disp('portin5 up- 15ms Stimulation Width Selected')
       stimWidth= 15  
end;
callback portin[5] down
	disp('portin5 down')
end;

% DIP #2 - +10ms Stimulation Width
callback portin[6] up
	disp('portin6 up- 10ms Stimulation Width Selected')
       stimWidth= 10
end;
callback portin[6] down
	disp('portin6 down')
end;

% DIP #3 - 1000ms Stimulation Width Selected
callback portin[7] up
	disp('portin7 up-Even Stimulation Width Selected')
	 stimWidth= stimFreq/2
end;
callback portin[7] down
	disp('portin7 down')
end;
	

