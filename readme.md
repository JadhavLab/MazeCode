# Adding modes
In order to add a new maze mode a few types of functions will need to be modded. These include

- restartStimulus_modeAware: how and when to change the stimuli
- Correct and Incorrect, maybe decideIfSkip handles everything about the logic determining whether ports should be counted as correct, incorrect or skipped
- recordPerformance: handles everything about how performance of the animal is recorded into the performance structure
- trialTypeSelect: controls hwo trials are labeled, which can determine the logic of how to handle a trial or change when stimulus restarts.

## Block trial cue memory training

An easier way to add this mode than modify all of these per se would to see check on the option of treating it like a version of the simultaneous mode that always ignores trial type. For a sequence of N = 1, it would always append a 5 to the beginning of the cue sequence.

After each poke, lockout might take care of the animal continuously poking the same well, otherwise, I will have to modify Correct() to have a mode-dependent status check.

restartStimulus_modeAware can then use the block correct counter to exit each restart request, until the block number is met. Another paramer can be used to control after how many trials the arena's well turns off.Skip
