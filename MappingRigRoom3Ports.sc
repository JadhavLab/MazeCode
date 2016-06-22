% AUTHOR: Suman K. Guha
% DESCRIPTION: Mapping of Ports


% CONSTANT DECLARATION
% ------------------------------------------------------------

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

% Defining reward delivery duration in miliseconds
int rewardDuration = 10000

% VARIABLE DECLARATION
% ------------------------------------------------------------

% FUNCTIONS SECTION
% ------------------------------------------------------------

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[1] up
  disp('Subject investigating Odor Well')
  portout[odorWellLED] = 1
  disp('Odor Well LED ON')
  do in 5000
    portout[odorWellLED] = 0
    disp('Odor Well LED OFF')
  end
end

callback portin[2] up
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

callback portin[3] up
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
