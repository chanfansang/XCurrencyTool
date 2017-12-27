package com.xmj.tool.service.common;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "BATCH_NUM,BERNAME,IDN,USERLABEL,NATIVE_EMS_NAME,CREATOR_NAME,TRAFFIC_TRUNK_NO,LAYER_RATE,TRANSMISSION_PARAMS,DIRECTION,ACTIVE_STATE,A_ENDS,Z_ENDS,RELATED_A_NE_NAME,RELATED_Z_NE_NAME,A_IN_LABEL,A_OUT_LABEL,Z_IN_LABEL,Z_OUT_LABEL,SERVICE_STATE,FULL_ROUTE,NETWORK_ROUTED,CONSISTENT_STATE,LSP_TYPE,CREATED_TIME,A2ZCIR,A2ZPIR,A2ZCBS,A2ZPBS,Z2ACIR,Z2APIR,Z2ACBS,SERVICE_TYPE,Z2APBS,BINDING_OBJECT,PROTECTION_EFFORT,REMARK,ISDELETE,CREATE_TIME,LAST_MODIFY_TIME";
				String aa = "BATCH_NUM,BERNAME,IDN,USERLABEL,NATIVE_EMS_NAME,CREATOR_NAME";
				String[] b = a.split(",");
				
				String c = ",EMS=Huawei/U2000:Flowdomain=1:TrafficTrunk=PWTRAIL=9,EMS=Huawei/U2000:MultiLayerSubnetwork=1:SubnetworkConnection=PWTRAIL=9,2014-武汉冷冻六厂/3900-9005-WHMD(DC)-1-PTN6-ETH-PW APS保护-97,2014-武汉冷冻六厂/3900-9005-WHMD(DC)-1-PTN6900-PWTRAIL=9,,9.0,8010.0,,CD_BI,SNCS_ACTIVE,EMS=Huawei/U2000:ManagedElement=2014-武汉冷冻六厂/3900:FTP=PW=2423|5||||,EMS=Huawei/U2000:ManagedElement=9005-WHMD(DC)-1-PTN6900:FTP=PW=|5|1500|1185||,2014-武汉冷冻六厂/3900,9005-WHMD(DC)-1-PTN6900,1185,,,1500,,,false,false,,,,,,,,,,,ETH,,\\name=EMS\\value=Huawei/U2000\\name=MultiLayerSubnetwork\\value=1\\name=SubnetworkConnection\\value=TUNNELTRAIL=5419,,,,,,";
				String[] d = c.split(",");
				
				System.out.println(b.length+","+d.length);
		String asa = "";
				for(int i=0;i<36;i++){
					asa += b[i]+"="+d[i]+",";
				}
				System.out.println(asa);
		
	}

}
