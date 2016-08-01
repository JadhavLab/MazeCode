% AUTHOR: Suman K. Guha
% DESCRIPTION: Mapping of Ports


% CONSTANT DECLARATION
% ------------------------------------------------------------

% Input Ports
int leftRewardWell = 1
int rightRewardWell = 2
int odorWell = 5

% Output Ports
int rewardWell = 0
int leftLED = 1
int rightLED = 2
int leftRewardWellPump = 3
int rightRewardWellPump = 4
int odorWellLED = 5;

% Defining reward delivery duration in miliseconds
int rewardDuration = 10000

% VARIABLE DECLARATION
% ------------------------------------------------------------

% FUNCTIONS SECTION
% ------------------------------------------------------------

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[5] up
  disp('Subject investigating Odor Well')
  portout[odorWellLED] = 1
  disp('Odor Well LED ON')
  do in 5000
    portout[odorWellLED] = 0
    disp('Odor Well LED OFF')
  end
end

callback portin[1] up
  disp('this is left reward well')
  portout[leftLED] = 1
  disp('Left Well LED ON')
  do in 5000
    portout[leftLED] = 0
    disp('Left Well LED OFF')
  end
  disp('Dispensing Reward in Left Well')
  portout[leftRewardWellPump] = 1
  do in 500
    portout[leftRewardWellPump] = 0
    disp('Terminating reward delivery')
  end
end

callback portin[2] up
  disp('this is right reward well')
  portout[rightLED] = 1
  disp('Right Well LED ON')
  do in 5000
    portout[rightLED] = 0
    disp('Right Well LED OFF')
  end
  disp('Dispensing Reward in Right Well')
  portout[rightRewardWellPump] = 1
  do in 500
    portout[rightRewardWellPump] = 0
    disp('Terminating reward delivery')
  end
end;
