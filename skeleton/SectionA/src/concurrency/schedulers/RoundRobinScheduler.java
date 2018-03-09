package concurrency.schedulers;

import concurrency.ConcurrentProgram;
import concurrency.DeadlockException;

import java.util.Collections;
import java.util.Iterator;

public class RoundRobinScheduler implements Scheduler {
  private boolean firstTime;
  private int lastExecutedId;
  public RoundRobinScheduler(){
    this.firstTime = true;
    this.lastExecutedId = -1;
  }
  public int chooseThread(ConcurrentProgram program) throws DeadlockException {
    if(program.getEnabledThreadIds().isEmpty()){
      throw new DeadlockException(); // Throw Exception
    }
    else{
      if(firstTime){
        firstTime = false;
        this.lastExecutedId = Collections.min(program.getEnabledThreadIds());
        return lastExecutedId;
      }
      else{
        Iterator<Integer> it = program.getEnabledThreadIds().iterator();
        int currMin = Integer.MAX_VALUE;
        while (it.hasNext()){
          int tmp = it.next();
          if(tmp < currMin && tmp > lastExecutedId){
            currMin = tmp;
          }
        }
        if(currMin > lastExecutedId && currMin != Integer.MAX_VALUE){
          lastExecutedId = currMin;
        }
        else {
          lastExecutedId = Collections.min(program.getEnabledThreadIds());
        }
        return lastExecutedId;
      }
    }
  }
}
