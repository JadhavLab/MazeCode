% PROGRAM NAME: LINEAR TRACK ROUNDS OF 10
% AUTHOR: Suman K. Guha
% DESCRIPTION: This program delivers reward from each reward well (on a linear track) when the beam is broken, and the animal successfully goes to the well that  has the LED on — each well has an LED separately wired to the ECU. After successful 10 trials, the reward well alternates. After that, Each epoch has two trials of 10 each. After that randomize the LED and reward dispension pattern to make sure direction is not being associated, rather just the LED with food.

% CONSTANT DECLARATION
% ------------------------------------------------------------

% Reward delivery duration in miliseconds
int rewardDuration = 500

% Input Ports
int odorWell = 1
int leftRewardWell = 2
int rightRewardWell = 3

% Output Ports
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
int rewardCounter = 0 % variable counting number of times rewarded

% FUNCTIONS SECTION
% ------------------------------------------------------------

% This function ends the trial
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

% This function checks the choice, switch off LED, and dispense reward. After finishing reward dispensation, swap active well and switch on active well LED and activate pump
function 2
  if (currentWell == activeWell && rewardCounter < 10) do
    portout[activeLED] = 0
    activeWell = 0
    portout[activePump] = 1
    disp('Rewarding ... ')
    do in rewardDuration
      portout[activePump] = 0
      disp('Rewarding complete.')
      rewardCounter = rewardCounter + 1
      disp(rewardCounter)
      if (currentWell == leftRewardWell) do
        activeWell = rightRewardWell
        activeLED = rightLED
        activePump = rightRewardWellPump
        disp('Right Well activated. Waiting on Subject ... ')
      end
      if (currentWell == rightRewardWell) do
        activeWell = leftRewardWell
        activeLED = leftLED
        activePump = leftRewardWellPump
        disp('Left Well activated. Waiting on Subject ... ')
      end
      portout[activeLED] = 1
      if (rewardCounter >= 10) do
        trigger(3)
      end
    end
  else do
    disp('Wrong Choice')
  end
end;

% This function starts the trial
function 1
  startTrial = 0 % setting flag to indicate trial started
  disp('Trial Started')
  activeLED = leftLED
  portout[activeLED] = 1
  activeWell = leftRewardWell % assigning the active well status
  activePump = leftRewardWellPump % assigning the active pump status
  disp('Left LED ON')
  disp('Left Well Pump Active, waiting on Subject ... ')
end;

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[1] up
  currentWell = odorWell
  disp('Poke in odor well')
  if (startTrial == 1) do
    trigger(1)
  else do
    disp('Wrong Choice')
  end
end

callback portin[2] up
  currentWell = leftRewardWell
  disp('Poke in left reward well')
  trigger(2)
end

callback portin[3] up
  currentWell = rightRewardWell
  disp('Poke in right reward well')
  trigger(2)
end;
