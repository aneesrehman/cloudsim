package org.cloudbus.cloudsim.examples;

// Java imports
import java.util.ArrayList;

// Cloudlet imports
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;

public class MinMin extends DatacenterBroker {

	public MinMin(String name) throws Exception {
		super(name);
	}

	public static void main(String[] args) {
		System.out.println("MinMin Algo Implementation");
	}
	
	/**
	 * Function to schedule Cloudlets against VMs based on Min-Min algo policy
	 */
	public void minMinScheduler() {
		int nTasks = cloudletList.size(); // Holds number of tasks
		int nVms = vmList.size(); // Holds number of vms
		System.out.println("VMSSS = " + nVms);
		
		double cTime[][] = new double[nTasks][nVms];
		double eTime[][] = new double[nTasks][nVms];
		
		// Copying over cloudlets
		
		ArrayList<Cloudlet> cList = new ArrayList<Cloudlet>();
		for ( Cloudlet cloudlet: getCloudletList() ) {
			cList.add(cloudlet);
		}
		
		double time = 0.0;
		
		// computing completion and execution time for each cloudlet over each vm
		for ( int i = 0; i < nTasks; i++ ) {
			for ( int j = 0; j < nVms; j++ ) {
				// 
				cTime[i][j] = getCompletionTime(cList.get(i), vmList.get(i));
				eTime[i][j] = getExecutionTime(cList.get(i), vmList.get(i));
				
				// Technically All tasks should be executed over first VM
			}
		}
		
		printTable(cTime, nTasks, nVms);
		
		for ( int i = 0; i < nTasks; i++ ) {
			int minTask = -1;
			int minVm = -1;
			double minCTime = -1.0;
			System.out.println("Here");
			for ( int j = 0; j < nTasks; j++ ) {
				
				// Search in all VMs
				for ( int k = 0; k < nVms; k++ ) {
					if ( cTime[j][k] > 0.0 && minCTime < 0.0 ) {
						minCTime = cTime[j][k];
						minTask = j;
						minVm = k;
					} else if ( cTime[j][k] > 0.0 && minCTime > cTime[j][k] ) {
						minCTime = cTime[j][k];
						minTask = j;
						minVm = k;
					}
					
				}
				
			}
			
			// At that point minTask with
			// j as task number
			// k as vm number
			// would be found
			
			// Bind both
			System.out.println("Executing Cloudlet #" + minTask + " on VM#" + minVm);
			bindCloudletToVm(minTask, minVm);
			
			// Set 0 as completion time for minTask
			for ( int j = 0; j < nVms; j++ ) {
				cTime[minTask][j] = 0.0;
			}
			
		}
		
		// Now for each task, find minimum number and bind task to vm
		// Reset it's value to 0 to be skipped over next iterations
	}
	
	/**
	 * Function to compute completion time for specific cloudlet over specified vm
	 * @param cloudlet
	 * @param vm
	 * @return
	 */
	private double getCompletionTime(Cloudlet cloudlet, Vm vm) {
		double waitingTime = cloudlet.getWaitingTime();
		double execTime = cloudlet.getCloudletLength() / (vm.getMips()*vm.getNumberOfPes());
		
		double completionTime = execTime + waitingTime;
		
		return completionTime;
	}
	
	/**
	 * Function to compute execution time for specific cloudlet over specified vm
	 * @param cloudlet
	 * @param vm
	 * @return
	 */
	private double getExecutionTime(Cloudlet cloudlet, Vm vm) {
		return cloudlet.getCloudletLength() / (vm.getMips()*vm.getNumberOfPes());
	}

	private void printTable(double cTime[][], int nTasks, int nVms) {
		for ( int i = 0; i < nTasks; i++ ) {
			for ( int j = 0; j < nVms; j++ ) {
				System.out.print(cTime[i][j] + "    ");
			}
			System.out.println("");
		}	
	}
	
}
