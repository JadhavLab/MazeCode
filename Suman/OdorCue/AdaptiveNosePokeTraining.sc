% PROGRAM NAME: Adaptive Nose Poke Training
% AUTHOR: Suman K. Guha
% DESCRIPTION: This program trains animals to nose poke from scratch and get to a specific duration to get a reward using a step-wise adaptive increment/decrement. The intention is to have a very fast cue which indicates reward dispension and to get from zero to a nose poke trained animal very fast.
% The strategy is initial instant reward with subsequent introduction of nose-poke hold duration. For the first 15 trials, upon nose poke play a beep for 250ms and dispense reward. The variable baselineAverageNosePokeDuration keeps a tab of the average duration that an animal nose pokes over this 15 trials. After the first 15 rewards, the trial moves automatically into phase 2. Here the animal *has to* nose poke for the baseline duration to get a reward.

In the next phase of this trial the duration of nose-poke is set at the average nose-poke duration.

% REGIME:
% A single epoch consists of a 10 min sleep box; 30 min of adaptive nose-poke–reward training; and another 10 mins of sleep box. Total epoch duration = 50 min.

% CONSTANT DECLARATION
% ------------------------------------------------------------
% Input Ports: ethernet port/ECU pin over which signal are sent *to* the ECU
int rewardWell = 1
int nosePokeWell = 5

%Output Ports: ethernet port/ECU pin over which signals are sent *from* the ECU
int rewardWellPump =
int buzzer =

% VARIABLE DECLARATION
% ------------------------------------------------------------


% CALLBACKS:EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

% when nose poke IR beam is broken
callback portin[5] up
  if (startTrial ==1) do
  end
end

% when nose poke IR beam is re-established
callback portin[5] down
  if (startTrial == 0) do
  else do
  end
end

% when reward well IR beam is broken
callback portin[1] up
  if (startTrial == 0) do
  end
end

% when reward well IR beam is re-established
callback portin[1] down
end
