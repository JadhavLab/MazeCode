int nextWell = 5
int count = 0;

callback portin[1] up
  if (count < 20) do
    while (nextWell == 5) do every 1
      nextWell = random(10)
    then do
      disp(nextWell)
      nextWell = 5
      count = count + 1
      disp(count)
    end
  else do
    disp('End of trial')
  end
end;
