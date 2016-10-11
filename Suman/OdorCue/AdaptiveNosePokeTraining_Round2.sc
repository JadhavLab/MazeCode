% PROGRAM NAME: Adaptive Nose Poke Training — Round 2
% AUTHOR: Suman K. Guha
% DESCRIPTION: This program trains animals to nose poke from scratch and get to a specific duration to get a reward using a step-wise adaptive increment/decrement. The intention is to have a very fast cue which indicates reward dispension and to get from zero to a nose poke trained animal very fast.
% The strategy is initial instant reward with subsequent introduction of nose-poke hold duration. For the first 15 trials, upon nose poke play a beep for 250ms and dispense reward. The variable baselineAverageNosePokeDuration keeps a tab of the average duration that an animal nose pokes over this 15 trials. After the first 15 rewards, the trial moves automatically into phase 2. Here the animal *has to* nose poke for the baseline duration to get a reward. In the next phase of this trial the duration of nose-poke is set at the average nose-poke duration.

% CONVENTIONS
% ------------------------------------------------------------
% 1. Because variables are printed as <variable name> = <variable value> most of the display statements are kept in that format for convenient post-acquisition data analysis
% 2. All display statement variables are UpperCamelCase while all calculated variables are lowerCamelCase for clear distinction

% REGIME:
% ------------------------------------------------------------
% A single epoch consists of a 10 min sleep box; 30 min of adaptive nose-poke–reward training; and another 10 mins of sleep box. Total epoch duration = 50 min
% During phase 0 the animal nose pokes, a beep is played, and reward is displayed. phase 0 is just for

% CONSTANT DECLARATION
% ------------------------------------------------------------
% Input Ports: ethernet port/ECU pin over which signal are sent *to* the ECU
int nosePokeWell = 5
int rewardWell = 1

% Output Ports: ethernet port/ECU pin over which signals are sent *from* the ECU
int buzzer = 9
int rewardWellPump = 3

% Duration of reward delivery in miliseconds
int rewardDuration = 500

% Maximum rewards that can be collected
int maxRewards = 250

% Buzzer duration
int buzzerDuration = 300

% Steps for increment or decrement of nose poke hold duration
int nosePokeHoldDurationDecrement =
int nosePokeHoldDurationIncrement =

% VARIABLE DECLARATION
% ------------------------------------------------------------
int baselineNosePokeHoldDuration = 0
int clockStart = 0
int currentClock = 0
int latencyToNosePoke = 0
int meanNosePokeHoldDuration = 0
int nosePokeHoldDuration = 0
int phase = 0
int requiredNosePokeHoldDuration = 0
int rewardCounter = 0
int sampleSize = 0
int startTrial = 0
int trialStartTime = 0

% FUNCTIONS SECTION
% ------------------------------------------------------------
% This function dispenses the reward, increments the rewardCounter by 1 and displays it, and depending on the phase calculates baselineNosePokeHoldDuration
function 1
  portout[rewardWellPump] = 1
  do in rewardDuration
    portout[rewardWellPump] = 0
    rewardCounter = rewardCounter + 1
    disp(rewardCounter)
  end
end;

% CALLBACKS: EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------
% when nose poke IR beam is broken
callback portin[5] up
  if (startTrial == 1) do
    disp('NosePoke = Engaged')
    clockStart = clock()
    currentClock = clock()
    latencyToNosePoke = currentClock - trailStartTime
    disp(latencyToNosePoke)
    startTrial = 2
  else if (startTrial == 2) do
    disp('NosePoke = Engaged')
    clockStart = clock()
  end
end

% when nose poke IR beam is re-established
callback portin[5] down
  if (startTrial == 0) do
    trialStartTime = clock()
    disp('TrialStatus = TrialStarted')
    startTrial = 1
  else if (startTrial == 2) do
    disp('NosePoke = Disengaged')
    % Note: using a special variable sampleSize to update mean nose poke duration and not re-purposing rewardCounter to avoid errors due to asynchronous execution in ECU/StateScript
    sampleSize = rewardCounter
    currentClock = clock()
    nosePokeHoldDuration = currentClock - clockStart
    disp(nosePokeHoldDuration)
    meanNosePokeHoldDuration = (((sampleSize * meanNosePokeHoldDuration) + nosePokeHoldDuration) / (sampleSize + 1))
    disp(meanNosePokeHoldDuration)
    if (phase == 0) do
      trigger(1)
    else if (phase == 1) do

    end
  end
end
