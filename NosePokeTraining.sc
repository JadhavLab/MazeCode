% PROGRAM NAME: Nose Poke Training
% AUTHOR: Suman K. Guha
% DESCRIPTION: This program delivers reward from a well that is right next to the nose-poke well. The intention is to form a very strong association with nose-poke and reward dispension. The reward is dispensed immediately as a nose poke is detected.

% REGIME:
% A single epoch consists of 10 min sleep box, 10 min run, 5 min sleep box, 10 min run, and a final 5 min sleep box session. After which the animals are provided with a treat and kept back in the home cage. The animals are weighed before and after the epoch.

% CONSTANT DECLARATION
% ------------------------------------------------------------

% Reward delivery duration in miliseconds
int rewardDuration = 500

% Input Ports
int nosePokeWell = 1

% Output Ports
int rewardWellPump = 3;

% VARIABLE DECLARATION
% ------------------------------------------------------------

int rewardCounter = 0 % variable counting number of times rewarded

% CALLBACKS:EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[1] up
  disp('successful nose poke')
  portout[rewardWellPump] = 1
  rewardCounter = rewardCounter + 1
  disp(rewardCounter)
  do in rewardDuration
    portout[rewardWellPump] = 0
  end
end;
