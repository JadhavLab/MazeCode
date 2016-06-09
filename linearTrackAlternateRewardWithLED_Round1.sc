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

function switchActiveWell
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

function dispenseReward
  disp('Now Rewarding')
  disp(rewardWell)
  portout[activePump] = 1 % deliver reward
  do in rewardDeliveryDuration
    portout[activePump] = 0 % switch off dispensing reward
  end
  successCounter = successCounter + 1
  disp('Times Rewarded:')
  disp(successCounter)
  trigger(switchActiveWell)
end;

function checkChoice
  if (currentWell == activeWell) do
    disp('Correct choice')
    rewardWell == currentWell
    trigger(dispenseReward)
  else do
    disp('Wrong choice')
  end;
end;

function endTrial
  disp('END OF TRIAL')
  disp('PRESS END TRIAL BUTTON')
end;

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[leftRewardWell] down
  if (trialStart == 1) do % These steps happen at start of trial
    portout[leftLED] = 1 % switching on left LED
    activeWell = leftRewardWell % activating left reward well
    disp('Starting Trial, left well active, left reward well LED ON, waiting on Subject')
    activePump = leftRewardWellPump
    trialStart = 0 % setting parameters to indicate trial started
  end
  if (successCounter >= 10) do
    trigger(endTrial)
  end
end;

callback portin[rightRewardWell] down
  if (successCounter >= 10) do
    trigger(endTrial)
  end
end;

callback portin[leftRewardWell] up
  disp('nose poke in left reward well')
  currentWell = leftRewardWell
  trigger(checkChoice)
end;

callback portin[rightRewardWell] up
  disp('nose poke in right reward well')
  currentWell = rightRewardWell
  trigger(checkChoice)
end;
