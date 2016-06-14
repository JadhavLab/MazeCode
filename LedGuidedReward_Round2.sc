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
int rewardWell = 0
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

function 1
  portout[activeLED] = 0
  if (activeWell == odorWell)
    if (startTrial == 1) do
    end
    if (lastWell = leftRewardWell)
    end
    if (lastWell == rightRewardWell)
  end
  if (activeWell == leftRewardWell || activeWell == rightRewardWell)
    lastWell = activeWell
  end
end;

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[1] down
  if (startTrial == 1) do
    activeWell = odorWell
    activeLED = odorWellLED
    portout[activeLED] = 1
    disp('Trial initiated, waiting on subject at odor well ...')
  end
end

callback portin[1] up
  currentWell = odorWell
  disp('Poke in odor well')
  if (currentWell == activeWell && rewardCounter < 10) do
    trigger(1)
  else do
    disp('Wrong choice')
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
