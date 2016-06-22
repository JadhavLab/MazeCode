% PROGRAM NAME: LINEAR TRACK WITH NOSE POKE IN ODOR WELL
% AUTHOR: Suman K. Guha
% DESCRIPTION: The purpose is the use the association that the animal made with LEDs in the last round to make them alternate between the odor well and the reward wells. LEDs lighting up suggest to the animal that they should investigate that well.
% REGIME: Odor well LED: Nose poke in odor well, activates a well, nose poke in that well dispenses reward. Repeat ad nauseam.
% NOTE: The operator has to initiate the trial by activating the odor well.

% CONSTANT DECLARATION
% ------------------------------------------------------------

% Reward delivery duration in miliseconds
int rewardDuration = 500

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
int activeWell = 1 % variable to assign active well
int activePump = 0 % variable to assign active pump
int activeLED = 0 % variable to assign active LED
int currentWell = 0 % variable to indicate the well picked by subject
int lastWell = 0 % variable to indicate the last well visited
int rewardCounter = 0 % variable counting number of times rewarded

% FUNCTIONS SECTION
% ------------------------------------------------------------

function 2
  rewardCounter = rewardCounter + 1
  if (rewardCounter == 10) do
    activeWell = 0
    activePump = 0
    activeLED = 0
    disp('End of Trial.')
    disp('Waiting on operator')
    disp('Total successful rewards:')
    disp(rewardCounter)
  else do
    activeLED = odorWellLED
    activeWell = odorWell
    portout[activeLED] = 1
    disp(rewardCounter)
    disp('Odor well LED ON ... ')
    disp('Waiting for subject at odor well ... ')
  end
end;

function 1
  if (startTrial == 1) do
    portout[odorWellLED] = 1
    lastWell = odorWell
    startTrial = 0
    disp('Trial initiated by operator ... ')
    disp('Odor well LED ON ... ')
    disp('Waiting for subject at odor well ... ')
  else do
    portout[odorWellLED] = 0
    if (random(2) < 1) do
      % sequence for activating left well
      activeWell = leftRewardWell
      activeLED = leftLED
      activePump = leftRewardWellPump
    else do
      % sequence for activating right well
      activeWell = rightRewardWell
      activeLED = rightLED
      activePump = rightRewardWellPump
    end
    portout[activeLED] = 1
  end
end;

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[1] up % odor port triggered
  currentWell = 1
  disp('Poke in odor well')
  if (activeWell == 1) do
    disp('Correct choice')
    portout[activeLED] = 0
    trigger(1)
    lastWell = odorWell
  else do
    disp('Wrong choice')
  end

end

callback portin[2] up % left well triggered
  currentWell = 2
  disp('Poke in left well')
  if (activeWell == 2) do
    disp('Correct choice')
    portout[activeLED] = 0
    trigger(2)
    lastWell = leftRewardWell
  else do
    disp('Wrong choice')
  end
end

callback portin[3] up % right well triggered
  currentWell = 3
  disp('Poke in right well')
  if (activeWell == 3) do
    disp('Correct choice')
    portout[activeLED] = 0
    trigger(2)
    lastWell = rightRewardWell
  else do
    disp('Wrong choice')
  end
end;
