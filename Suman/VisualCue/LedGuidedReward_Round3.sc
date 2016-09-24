% PROGRAM NAME: LINEAR TRACK WITH NOSE POKE IN ODOR WELL

% AUTHOR: Suman K. Guha

% DESCRIPTION: The purpose is the use the association that the animal made with LEDs in the last round to make them alternate between the odor well and the reward wells. LEDs lighting up suggest to the animal that they should investigate that well. The random() function is used to decide which well the animal should investigate.

% REGIME: Odor well LED: Nose poke in odor well, activates a well, nose poke in that well dispenses reward. Repeat till 10 rewards are dispensed in total.

% NOTE: The operator has to initiate the trial by activating the odor well.

% CONSTANT DECLARATION
% ------------------------------------------------------------

% Reward delivery duration in miliseconds and maximum rewards to be dispensed
int rewardDuration = 1250
int maxReward = 999

% Input Ports
int odorWell = 5
int leftRewardWell = 1
int rightRewardWell = 2

% Output Ports
int leftLED = 1
int rightLED = 2
int leftRewardWellPump = 3
int rightRewardWellPump = 4
int odorWellLED = 5
int leftIndicator = 6
int rightIndicator = 7;

% VARIABLE DECLARATION
% ------------------------------------------------------------

int startTrial = 1 % flag indicating trial should be started
int activeWell = 5 % variable to assign active well
int activePump = 0 % variable to assign active pump
int activeOdorPump = 0
int activeLED = 0 % variable to assign active LED
int activeIndicator = 0 % variable to assign active indicator
int currentWell = 0 % variable to indicate the well picked by subject
int lastWell = 0 % variable to indicate the last well visited
int rewardCounter = 0 % variable counting number of times rewarded
int nextWell = 0 % variable for deciding in which well the reward will be dispensed.

% FUNCTIONS SECTION
% ------------------------------------------------------------

function 3
  portout[activeLED] = 0
  activeWell = 0
  activePump = 0
  activeLED = 0
  disp('End of Trial.')
  disp('Waiting on operator')
  disp('Total successful rewards:')
  disp(rewardCounter)
end;

function 2
  if (currentWell == activeWell && rewardCounter < maxReward) do
    portout[activeLED] = 0
    portout[activeIndicator] = 0
    activeWell = 0
    portout[activePump] = 1
    disp('Rewarding ... ')
    do in rewardDuration
      portout[activePump] = 0
      disp('Rewarding complete.')
      rewardCounter = rewardCounter + 1
      disp(rewardCounter)
      activeWell = odorWell
      activeLED = odorWellLED
      disp('Odor Well activated. Waiting on Subject ... ')
      portout[activeLED] = 1
      if (rewardCounter >= maxReward) do
        activePump = 0
        trigger(3)
      end
    end
  else do
    disp('Wrong Choice')
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
    nextWell = random(10000)
    if (nextWell == 5000) do
      while (nextWell == 5000) do every 1
        nextWell = random(10000)
      end
    end
    if (nextWell < 5000) do
      % sequence for activating left well
      activeWell = leftRewardWell
      activeLED = leftLED
      activeIndicator = leftIndicator
      activePump = leftRewardWellPump
      disp('left reward well activated')
    else do
      % sequence for activating right well
      activeWell = rightRewardWell
      activeLED = rightLED
      activeIndicator = rightIndicator
      activePump = rightRewardWellPump
      disp('right reward well activated')
    end
    portout[activeLED] = 1
    portout[activeIndicator] = 1
  end
end;

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[5] up % odor port triggered
  currentWell = odorWell
  disp('Poke in odor well')
  if (activeWell == 5) do
    disp('Correct choice')
    portout[activeLED] = 0
    portout[activeIndicator] = 0
    trigger(1)
    lastWell = odorWell
  else do
    disp('Wrong choice')
  end

end

callback portin[1] up % left well triggered
  currentWell = leftRewardWell
  disp('Poke in left well')
  if (activeWell == 1) do
    disp('Correct choice')
    portout[activeLED] = 0
    portout[activeIndicator] = 0
    trigger(2)
    lastWell = leftRewardWell
  else do
    disp('Wrong choice')
  end
end

callback portin[2] up % right well triggered
  currentWell = rightRewardWell
  disp('Poke in right well')
  if (activeWell == 2) do
    disp('Correct choice')
    portout[activeLED] = 0
    portout[activeIndicator] = 0
    trigger(2)
    lastWell = rightRewardWell
  else do
    disp('Wrong choice')
  end
end;
