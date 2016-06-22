% CONSTANT DECLARATION
% ------------------------------------------------------------

% Pump activity duration in miliseconds
int pumpActive = 500

% Input and output ports are mapped using the MappingRigRoom3Ports.sc script
% Input Ports
int odorWell = 1
int leftRewardWell = 2
int rightRewardWell = 3

% Output Ports
int rewardWell = 0
int leftRewardWellPump = 1
int rightRewardWellPump = 2
int odorWellLED = 5
int leftLED = 6
int rightLED = 7;

% VARIABLE DECLARATION
% ------------------------------------------------------------

% FUNCTIONS SECTION
% ------------------------------------------------------------

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[2] up
  portout[leftRewardWellPump] = 1
  do in pumpActive
    portout[leftRewardWellPump] = 0
    disp('left well primed')
  end
end

callback portin[3] up
  portout[rightRewardWellPump] = 1
  do in pumpActive
    portout[rightRewardWellPump] = 0
    disp('right well primed')
  end
end;
