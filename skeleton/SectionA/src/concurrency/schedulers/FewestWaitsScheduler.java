package concurrency.schedulers;

import concurrency.ConcurrentProgram;
import concurrency.DeadlockException;
import concurrency.statements.Stmt;
import concurrency.statements.WaitStmt;

import java.util.Collection;
import java.util.Iterator;

public class FewestWaitsScheduler implements Scheduler {
  public FewestWaitsScheduler(){

  }
  @Override
  public int chooseThread(ConcurrentProgram program) throws DeadlockException {
    if (program.getEnabledThreadIds().isEmpty()) {
      throw new DeadlockException();
    } else {
      Iterator<Integer> it = program.getEnabledThreadIds().iterator();
      int id = Integer.MAX_VALUE;
      int noOfWaits = Integer.MAX_VALUE;
      while(it.hasNext()){
        int tmpId = it.next();
        int tmpWaits = countWaits(tmpId,program);
        if(tmpWaits < noOfWaits){
          id = tmpId;
          noOfWaits = tmpWaits;
        }
        if(tmpWaits == noOfWaits){
          if(tmpId < id){
            id = tmpId;
          }
        }
      }
      return id;
    }
  }

  private int countWaits(int threadId,ConcurrentProgram program){
    Collection stmts = program.remainingStatements(threadId);
    int count = 0;
    Iterator<Stmt> it = stmts.iterator();
    while(it.hasNext()){
      if(it.next() instanceof WaitStmt){
        count++;
      }
    }
    return count;
  }
}
