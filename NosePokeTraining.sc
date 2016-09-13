% PROGRAM NAME: Nose Poke Training
% AUTHOR: Suman K. Guha
% DESCRIPTION: This program delivers reward from a well that is right next to the nose-poke well. The intention is to form a very strong association with nose-poke and reward dispension. The reward is dispensed immediately as a nose poke is detected.

% REGIME:
% A single epoch consists of 15 min sleep box, 15 min run, 15 min sleep box, 15 min run, and a final 5 min sleep box session.

% CONSTANT DECLARATION
% ------------------------------------------------------------

% Reward delivery duration in miliseconds
int rewardDuration = 500
int maxReward = 999

% Input Ports
int nosePokeWell = 1

% Output Ports
int rewardWellPump = 3;

% VARIABLE DECLARATION
% ------------------------------------------------------------

int rewardCounter = 0 % variable counting number of times rewarded

% CALLBACKS:  EVENT-DRIVEN TRIGGERS
% ------------------------------------------------------------

callback portin[1] up
  disp('successful nose poke')
  portout[rewardWellPump] = 1
  rewardCounter = rewardCounter + 1
  disp('Successful rewards:')
  disp(rewardCounter)
  do in rewardDuration
    portout[rewardWellPump] = 0
  end
end;
