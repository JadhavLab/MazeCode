% CONSTANT DECLARATION
% ------------------------------------------------------------

% Pump activity duration in miliseconds
int pumpActive = 500

% Input and output ports are mapped using the MappingRigRoom3Ports.sc script
% Input Ports
int odorWell = 5
int leftRewardWell = 1
int rightRewardWell = 2

% Output Ports
int rewardWell = 0
int leftRewardWellPump = 3
int rightRewardWellPump = 4
int odorWellLED = 5
int leftLED = 1
int rightLED = 2;

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
