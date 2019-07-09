AV.Cloud.beforeSave('_User', async function(request) {
	
	var privilege = ["BROWSE_COMMUNITY_SHOW","BROWSE_COMMUNITY_TOPIC","BROWSE_I_ATTENDED"];
	request.object.set('privilege', privilege);	

}
