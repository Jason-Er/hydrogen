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

AV.Cloud.define('_messageReceived', async function(request) {
	
	console.log('request.params')
    console.log(request.params)
    let content = request.params.content;
	let timestamp = request.params.timestamp;
	let convId = request.params.convId;
	console.log('convId:'+convId);
	let query = new AV.Query('_Conversation');
	query.get(convId).then(function(conv) {		
		let topicId = conv.get('topic').id;
		console.log('topicId:'+topicId);
		let query = new AV.Query('Topic');
		query.get(topicId).then(function(topic) {
			let line = {};
			let contentObj = JSON.parse(content);
			console.log(contentObj);
			line['when'] = timestamp;
			line['what'] = contentObj['_lctext'];
			line['who'] = request.params.fromPeer;
			line['message'] = 'TEXT';
			let attrs = contentObj['_lcattrs'];
			console.log(attrs)
			line['type'] = attrs['type'];
			let dialogue = topic.get('dialogue');
			if(!dialogue) {
				dialogue = [];
			}
			dialogue.push(line);
			console.log(dialogue)
			topic.set('dialogue', dialogue);
			topic.save();
		});	
	});	
	
	return {
		content: content
	};
	
})