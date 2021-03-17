%WMaze code. 

int deliverPeriod = 500  
int delayperiod = 20000

int lastSideWell= 0           
int lastWell=0		 	
int currWell= 0            	

int rewardWell= 0       	
int nowRewarding = 0 	
int centerdoor = 30

int rewardPump1 = 5
int rewardPump2 = 6
int rewardPump3 = 7

updates off 16

function 1
	nowRewarding = 1 							
	portout[rewardWell]=1 							
	do in deliverPeriod 								
		portout[rewardWell]=0 							
		nowRewarding=0 				
	end				     

end;

function 2
	nowRewarding = 1 									
	portout[rewardWell]=1 					
	do in deliverPeriod 									
		portout[rewardWell]=0 							
		nowRewarding=0 				
	end				     

	portout[centerdoor]=1 
	do in delayperiod 
		portout[centerdoor] = 0 
	end
end;

function 7
	nowRewarding = 1 							
	portout[rewardWell]=1 							
	do in deliverPeriod 								
		portout[rewardWell]=0 							
		nowRewarding=0 				
	end	
end;	

function 3
	if lastSideWell == 0 && (currWell==1 || currWell == 3) do
		if currWell == 1 do
			rewardWell= rewardPump1
 		end
		if currWell == 3 do
			rewardWell= rewardPump3
 		end
		trigger(7)
	end
end;
 

function 6
	if lastWell==0 && currWell == 2 do
		rewardWell=rewardPump2
		trigger(7)
	end
end;


	

callback portin[2] up
	disp('Portin1 up - Left well on') 		
	currWell=1							 
	trigger(3)								
	if lastWell == 2 do					
		if lastSideWell == 3	do			
			disp('Poke 1 rewarded - left ')
			disp('Laser on - Left')
			rewardWell=rewardPump1 				
			trigger(1)					
		end
	else do
		disp('Poke 1 not rewarded - left')
	end
end

callback portin[2] down
	disp('Portin1 down - Left well off') 	
	lastWell = 1 							
	lastSideWell  = 1
end;



callback portin[3] up
	disp('Portin2 up - Center well on') 	
	currWell = 2
	trigger(6) 								
	if lastWell == 1 || lastWell == 3 do 	
		disp('Poke 2 rewarded - center')
		disp('Laser on - Center')
		rewardWell = rewardPump2
		trigger(2)
	else do
		disp('Poke 2 not rewarded - center')
	end
end;

callback portin[3] down
	disp('Portin2 down - Center well off')			
	lastWell=2								
end;


callback portin[8] up
	disp('Portin3 up - Right well on')						
	currWell = 3 						
	trigger(3)								
	if lastWell == 2 do					
		if lastSideWell == 1	do			
			disp('Poke 3 rewarded - right')
			disp('Laser on - Right')
			rewardWell=rewardPump3 				
			trigger(1) 					
		else do
			disp('Poke 3 not rewarded - right')
		end
	end
end;

callback portin[8] down
	disp('Portin3 down - Right well off')
	lastWell=3 								
	lastSideWell = 3
end;


