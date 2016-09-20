% PROGRAM NAME: Nose Poke Training
% AUTHOR: Suman K. Guha
% DESCRIPTION: This program delivers reward from a well that is right next to the nose-poke well, as an LED backlight turns on. The intention is to form a very strong association with nose-poke, neutral light cue, and reward dispension. The reward is dispensed immediately as a nose poke is detected and for round 1, the led backlight is on as the reward is being dispensed.

% REGIME:
% A single epoch consists of 3 min sleep box, 7.5 min run, 3 min sleep box, 7.5 min run, and a final 3 min sleep box session. After which the animals are provided with a treat and kept back in the home cage. The animals are weighed before and after the epoch.

% CONSTANT DECLARATION
% ------------------------------------------------------------

% Reward delivery duration in miliseconds
int rewardDuration = 500

% Input Ports
int nosePokeWell = 5

% Output Ports
int ledBacklight = 1
int rewardWellPump = 3;

% VARIABLE DECLARATION
% ------------------------------------------------------------

int rewardCounter = 0 % variable counting number of times rewarded

% CALLBACKS:EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[5] up
  disp('successful nose poke')
  portout[ledBacklight] = 1
  portout[rewardWellPump] = 1
  rewardCounter = rewardCounter + 1
  disp(rewardCounter)
  do in rewardDuration
    portout[ledBacklight] = 0
    portout[rewardWellPump] = 0
  end
end;
