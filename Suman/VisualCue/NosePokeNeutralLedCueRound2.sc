% PROGRAM NAME: Nose Poke Hold Training
% AUTHOR: Suman K. Guha
% DESCRIPTION: This program delivers reward from a well that is right next to the nose-poke well, as an LED backlight turns off, after a set time delay. The intention is to train the animals to pay attention to a neutral light cue, and continue nose-poke till the light is on, and that reward will be dispensed only if this task is done successfully.

% REGIME:
% A single epoch consists of 5 min sleep box, 10 min run, 5 min sleep box, 10 min run, and a final 5 min sleep box session. After which the animals are provided with a treat and kept back in the home cage. The animals are weighed before and after the epoch.

% CONSTANT DECLARATION
% ------------------------------------------------------------

int rewardDuration = 500 % duration of reward delivery in miliseconds
int timeDelay = 500 % time delay for which the rat has to hold the nose poke in miliseconds

% Input Ports: indicate the ethernet port/ECU pin over which it transmits signal
int nosePokeWell = 5

% Output Ports: each indicates the ethernet port/ECU pin over which it receives signal
int ledBacklight = 1
int rewardWellPump = 3;

% VARIABLE DECLARATION
% ------------------------------------------------------------

int rewardCounter = 0 % variable counting number of times rewarded
int startTrial = 0 % variable that indicates whether trial has started or not
int clockStart = 0 % variable that indicates time stamp of nose poke
int nosePokeDuration = 0 % variable to calculate duration of nose poke held
int currentClock = 0 % variable to store current clock

% CLOCK RESET to zero out clock
clock(reset);

% CALLBACKS:EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[5] up
  if (startTrial == 1) do
    clockStart = clock()
    disp('nose poke initiated')
    portout[ledBacklight] = 1
    do in timeDelay
      portout[ledBacklight] = 0
    end
  end
end

callback portin[5] down
  if (startTrial == 0) do
    disp('trial started')
    startTrial = 1
    disp(startTrial)
  else do
    currentClock = clock()
    nosePokeDuration = currentClock - clockStart
    if nosePokeDuration >= timeDelay do
      portout[ledBacklight] = 0
      disp('success')
      rewardCounter = rewardCounter + 1
      disp(rewardCounter)
      portout[rewardWellPump] = 1
      do in timeDelay
        portout[rewardWellPump] = 0
      end
    else do
      portout[ledBacklight] = 0
      disp('failure')
    end
  end
end;
