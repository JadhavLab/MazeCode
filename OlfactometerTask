%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%%  Name:         OlfactometerTask
%%
%%  Purpose:      Runs an olfactometer based task, selecting one of two
%%               smells to emit, and then rewards the animal based on whether
%%               it makes a trajectory to the proper arm.
%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%
%%   VARIABLE SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%

% ---------------------
% Digital Input Assignments
% ----------------------
int sample_arm = 1;
int left_arm = 2;
int right_arm  = 3;

% ---------------------
% Digital Output Assigments
% ----------------------
int left_reward = 1;
int right_reward = 2;

% ---------------------
% Odorant Delivery Parameters
% ----------------------
int smell_delivery_period = 2000; % milliseconds

% ---------------------
% Odor to Path Variables
% ----------------------
int PATH_ONE_ODOR = 1;
int PATH_TWO_ODOR = 2;

% ---------------------
% Behavior Trackers
% ---------------------
int sampled_smell = 0;

% ---------------------
% Apparatus Tracker
% ----------------------
int smell_picked = 0;

%% DETECTION OF POKE
% When a poke is detected, we will select a smell to randomly
% associate with the smell port.

%%%%%%%%%%%%%%%%%%%%%%%%%
%%   CALLBACK SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%

callback portin[sample_arm]

    if sampled_smell do
        % Pick random smell
        smell_picked = random(2);

        % Administer smell selected
        trigger(2);

    else do
        % NOTHING, rat already got a whiff of the sample
    end


end;

callback portin[left_arm]


end;

callback portin[right_arm]


end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%   HELPER FUNCTION SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% ---------------------
% Name:      ChooseRandomScent (1)
% Purpose:   Select a random scent to trigger
% ---------------------
function 1
end

% ---------------------
% Name:      AdministerSmell (1)
% Purpose:   Select a random scent to trigger
% ---------------------
function 2
    do in smell_deliever_period
         portout[smell_picked] = 1;
    end
end
