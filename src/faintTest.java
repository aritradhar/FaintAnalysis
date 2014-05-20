
// Aritra Dhar
// Program Analysis
//Faint variable analysis
//M.Tech CSE-IS
//MT12004
//IIIT-Delhi



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.AbstractHost;
import soot.tagkit.SourceLnPosTag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.util.Chain;


class faintTest {
	public void fun() {
		int x=1;
		int y=2;
		x=x-1;
		x=2;
		y=x+y;
		//x=10;
		y=x;
	}
	
	public void fun1() {
		int x=2;
		x=x-1;
		int z=x+4;
		System.out.println("fun2");
	}

}

class CreateJimple
{

	public static HashMap<String,Integer> Used_local;
	public static HashMap<String,Integer> Dead_local;
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public static void main(String[] ar)
	{
		//load class
		if(ar.length==0)
			System.out.println("Enter the class name to be analyszed");
		
		SootClass cls = Scene.v().loadClassAndSupport(ar[0]);	
		
		//cls.setApplicationClass(); 
		
		Integer method_count=cls.getMethodCount();
		for(int i=1;i<method_count;i++) // we don't need <init method>
		{
			Used_local=new HashMap<>();
			
			HashMap<List<ValueBox>,Unit> Used_variable=new HashMap<>();		
			SootMethod sm=cls.getMethods().get(i);
			//System.out.println(sm.retrieveActiveBody());
			Body body=sm.retrieveActiveBody();
			JimpleBody jm_body=(JimpleBody) sm.retrieveActiveBody();
			System.out.println(jm_body);
			
			Chain<Local> local_chain=jm_body.getLocals();
			List<String> local_list=new ArrayList<>();
			Iterator it_loc=local_chain.iterator();
			while(it_loc.hasNext())
			{
				String tmp_var=it_loc.next().toString();
				local_list.add(tmp_var);
				Used_local.put(tmp_var, 0);
			}
			
			//System.out.println(Used_local);
			//System.out.println(local_chain);
			//Local l=local_chain.getSuccOf(local_chain.getFirst());
			
			BriefUnitGraph bf=new BriefUnitGraph(jm_body);
			//System.out.print(bf);
			
			PatchingChain<Unit> unt=jm_body.getUnits();
			Iterator<Unit> unt_it=unt.iterator();
			List<Integer> lineNumbers = new ArrayList<Integer>();
			while(unt_it.hasNext())
			{
				Unit temp_unt=unt_it.next();
				//System.out.println("UNIT "+temp_unt);
				
				if(temp_unt instanceof AssignStmt)// || temp_unt instanceof IdentityStmt)
				{
					//check it later
					//SourceLnPosTag tag = (SourceLnPosTag) temp_unt.getTag("SourceLnPosTag");
					//if(tag!=null)
			         //lineNumbers.add(tag.startLn());
					
					//System.out.println(((DefinitionStmt) temp_unt).getLeftOpBox());
					//System.out.println(((DefinitionStmt) temp_unt).getLeftOp());
					//System.out.println(((DefinitionStmt) temp_unt).getRightOpBox());
					//System.out.println(((DefinitionStmt) temp_unt).getRightOp());
					//System.out.println(((DefinitionStmt) temp_unt).getUseBoxes());
					
					List<ValueBox> list_val_box=((DefinitionStmt) temp_unt).getUseBoxes();
					Iterator<ValueBox> it_value_box=list_val_box.iterator();
					
					Value Right_list_val_box=((DefinitionStmt) temp_unt).getLeftOp();
					//here the var is reassigned so it is killed. So make its use as 0.
					//if it is already 1 then make it zero
					if(Used_local.get(Right_list_val_box.toString())==1)
						Used_local.put(Right_list_val_box.toString(), (Used_local.get(Right_list_val_box.toString()) + 1)%2);
					
					
					while(it_value_box.hasNext())
					{
						ValueBox vb=it_value_box.next();
						String varSt="";
						if(vb.toString().contains("LinkedRValueBox") || vb.toString().contains("ImmediateBox"))
						{
							if(!vb.toString().contains(" + ") && !vb.toString().contains(" - ") && 
							   !vb.toString().contains(" * ") && !vb.toString().contains(" / "))
							{
								varSt=vb.toString().substring(vb.toString().indexOf("(")+1,vb.toString().indexOf(")"));
								//check if it is a propeer variable not constant
								if(local_list.contains(varSt))
								{
									System.out.println("Used variable: "+ varSt);
									if(Used_local.get(varSt)==0)
										Used_local.put(varSt, (Used_local.get(varSt) + 1)%2);
								}
							}
						}
						//System.out.println("##"+varSt);
					}
					if(!Used_variable.containsKey(((DefinitionStmt) temp_unt).getUseBoxes()))
						Used_variable.put((List<ValueBox>) ((DefinitionStmt) temp_unt).getUseBoxes(), temp_unt);
					//System.out.println(temp_unt);
				}
			}
			//System.out.println("Line :: "+lineNumbers);
			//System.out.println(Used_variable);
			System.out.println(Used_local);
			
			Iterator<Local> it=local_chain.iterator();
			//while(it.hasNext())				
				//System.out.println(it.next());
			PatchingChain<Unit> unit_chain=body.getUnits();
			//System.out.println(body.getUnits());
			//System.out.println("Method name : "+sm);
			System.out.println("---------------------");
		}
	}
	
}
