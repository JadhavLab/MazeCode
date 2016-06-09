% PROGRAM NAME: LINEAR TRACK ROUNDS OF 10
% AUTHOR: Suman K. Guha
% DESCRIPTION: This program delivers reward from each reward well (on a linear track) when the beam is broken, and the animal successfully goes to the well that  has the LED on — each well has an LED separately wired to the ECU. After successful 10 trials, the reward well alternates. After that, Each epoch has two trials of 10 each. After that randomize the LED and reward dispension pattern to make sure direction is not being associated, rather just the LED with food.

% CONSTANT DECLARATION
% ------------------------------------------------------------

% Reward delivery duration in miliseconds
int rewardDeliveryDuration = 500

% Input Ports
int odorWell = 1
int leftRewardWell = 2
int rightRewardWell = 3

% Output Ports
int leftRewardWellPump = 1
int rightRewardWellPump =2
int odorWellLED = 5
int leftLED = 6
int rightLED = 7;

% VARIABLE DECLARATION
% ------------------------------------------------------------

int trialStart = 1
int currentWell = 0
int activeWell = leftRewardWell
int successCounter = 0;

% FUNCTIONS SECTION
% ------------------------------------------------------------
% this function switches active well
function 1
  disp('Switching active reward well and pump')
  if (activeWell == leftRewardWell) do
    activeWell = rightRewardWell
    activePump = rightRewardWellPump
  end
  if (activeWell == rightRewardWell) do
    activeWell = leftRewardWell
    activePump = leftRewardWellPump
  end
end;

% this function dispenses reward
function 2
  disp('Now Rewarding')
  disp(rewardWell)
  portout[activePump] = 1 % deliver reward
  do in rewardDeliveryDuration
    portout[activePump] = 0 % switch off dispensing reward
  end
  successCounter = successCounter + 1
  disp('Times Rewarded:')
  disp(successCounter)
  trigger(1)
end;

% this function checks and verifies the choice made by the subject. If correct choice, then triggers the function to dispense reward
function 3
  if (currentWell == activeWell) do
    disp('Correct choice')
    rewardWell == currentWell
    trigger(2)
  else do
    disp('Wrong choice')
  end;
end;

% this function asks the operator to end the trial
function 4
  disp('END OF TRIAL')
  disp('PRESS END TRIAL BUTTON')
end;

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[2] down
  if (trialStart == 1) do % These steps happen at start of trial
    portout[leftLED] = 1 % switching on left LED
    activeWell = leftRewardWell % activating left reward well
    disp('Starting Trial, left well active, left reward well LED ON, waiting on Subject')
    activePump = leftRewardWellPump
    trialStart = 0 % setting parameters to indicate trial started
  end
  if (successCounter >= 10) do
    trigger(4)
  end
end;

callback portin[3] down
  if (successCounter >= 10) do
    trigger(4)
  end
end;

callback portin[2] up
  disp('nose poke in left reward well')
  currentWell = leftRewardWell
  trigger(3)
end;

callback portin[3] up
  disp('nose poke in right reward well')
  currentWell = rightRewardWell
  trigger(3)
end;
