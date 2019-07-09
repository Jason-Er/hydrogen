AV.Cloud.beforeSave('_User', async function(request) {
	
	var privilege = ["BROWSE_COMMUNITY_SHOW","BROWSE_COMMUNITY_TOPIC","BROWSE_I_ATTENDED"];
	request.object.set('privilege', privilege);	

}

AV.Cloud.afterSave('Topic', async function(request) {

	var topicId = request.object.get('derive_from').id;	
	var query = new AV.Query('Topic');
	query.get(topicId).then(function(topic) {
	    var privilege = topic.get('privilege');
		if(!privilege) {		
			privilege = {};
		}
		var sponsorId = request.object.get('sponsor').id;
		console.log('sponsorId:'+sponsorId)
		privilege[sponsorId] = ['PUBLISH'];
		console.log(privilege);
		console.log('request.object.id:'+request.object.id)		
		query.get(request.object.id).then(function(topic) {
			topic.set('privilege', privilege);
			topic.save()
		});		
	});	
	
}