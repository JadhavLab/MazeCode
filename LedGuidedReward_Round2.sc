% PROGRAM NAME: LINEAR TRACK WITH NOSE POKE IN ODOR WELL
% AUTHOR: Suman K. Guha
% DESCRIPTION: The purpose is the use the association that the animal made with LEDs in the last round to make them alternate between the odor well and the reward wells. LEDs lighting up suggest to the animal that they should investigate that well.
% REGIME: Odor well LED: Nose poke in odor well, activates a well, nose poke in that well dispenses reward. Repeat ad nauseam.
% NOTE: The operator has to initiate the trial by activating the odor well. odor well port going down initiates the trial

% CONSTANT DECLARATION
% ------------------------------------------------------------

% Reward delivery duration in miliseconds
int rewardDuration = 50

% Input Ports
int odorWell = 1
int leftRewardWell = 2
int rightRewardWell = 3

% Output Ports
% int odorWellPump =
int leftRewardWellPump = 1
int rightRewardWellPump = 2
int odorWellLED = 5
int leftLED = 6
int rightLED = 7;

% VARIABLE DECLARATION
% ------------------------------------------------------------

int startTrial = 1 % flag indicating trial should be started
int activeWell = 0 % variable to assign active well
int activePump = 0 % variable to assign active pump
int activeLED = 0 % variable to assign active LED
int currentWell = 0 % variable to indicate the well picked by subject
int lastWell = 0 % variable to indicate the last well visited
int rewardCounter = 0 % variable counting number of times rewarded

% FUNCTIONS SECTION
% ------------------------------------------------------------

% This function ends the trial
function 4
  portout[activeLED] = 0
  activeWell = 0
  activePump = 0
  activeLED = 0
  disp('End of Trial.')
  disp('Waiting on operator')
  disp('Total successful rewards:')
  disp(rewardCounter)
end;

% This function dispenses the reward in appropriate well
function 3
  portout[activeWell] = 1 % start dispensing reward
  disp('Rewarding ... ')
  do in rewardDuration
    portout[activeWell] = 0 % stop dispensing reward
    disp('Rewarding complete.')
    rewardCounter = rewardCounter + 1
    lastWell = activeWell
  end
  if (rewardCounter >= 10) do
    trigger(4)
  end
end;

% This function initiates the trial and primes the left reward well
function 2
  if (startTrial == 1) do
    startTrial = 0 % setting flag to indicate start of trial
    activeLED = leftLED % setting flag to indicate the current active led
    activeWell = leftRewardWell % setting the active well flag
    activePump = leftRewardWellPump % setting the active pump flag
    lastWell = odorWell % set flag to indicate last visit
    disp('Subject poked odor well, waiting for subject at left well ... ')
  end
  if (lastWell == leftRewardWell) do
    activeWell = rightRewardWell
    activeLED = rightLED
    activePump = rightRewardWellPump
    disp('Right Well activated. Waiting on Subject ... ')
  end
  if (lastWell == rightRewardWell) do
    activeWell = leftRewardWell
    activeLED = leftLED
    activePump = leftRewardWellPump
    disp('Left Well activated. Waiting on Subject ... ')
  end
end;

% This function decides whether the subject made the right choice, sets the variables correctly, toggles the LEDs, and dispenses reward
function 1
  portout[activeLED] = 0
  if (activeWell == odorWell)
    trigger(2)
  end
  if (activeWell == leftRewardWell || activeWell == rightRewardWell)
    trigger(3)
  end
  activeWell = odorWell
  activeLED = odorWellLED
  % activePump = odorWellPump
  portout[activeLED] = 1 % LED turned on in appropriate well
end;

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[1] up
  currentWell = odorWell
  if (startTrial == 1) do
    activeWell = odorWell % setting the odor well as active
    activeLED = odorWellLED % setting the odor well led as active
    portout[activeLED] = 1 % switching on the active (odor well) led
    disp('Trial initiated, waiting on subject at odor well ...')
  end
  else do
    disp('Poke in odor well')
    if (currentWell == activeWell && rewardCounter < 10) do
      trigger(1)
    else do
      disp('Wrong choice')
    end
  end
end

callback portin[2] up
  currentWell = leftRewardWell
  disp('Poke in left reward well')
  if (currentWell == activeWell && rewardCounter < 10) do
    trigger(1)
  else do
    disp('Wrong choice')
  end
end

callback portin[3] up
  currentWell = rightRewardWell
  disp('Poke in right reward well')
  if (currentWell == activeWell && rewardCounter < 10) do
    trigger(1)
  else do
    disp('Wrong choice')
  end
end;
