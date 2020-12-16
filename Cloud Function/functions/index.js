
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

exports.pushNotification = functions.database.ref('/smart/Lock').onWrite( event => {
	let tokens=[];
    return admin.database().ref('/Tokens').once('value')
                    .then(function(tokensSnapshot){
						 tokensSnapshot.forEach(function(childSnapshot) {
							 tokens.push(childSnapshot.child("token_id").val());
      
       });
       // const tokens = Object.keys(tokensSnapshot.val());
         console.log("token: ", tokens );
		 let msg = 'Password Changed Successfully';
        const payload = {
            notification: {
                title: 'Smart Lock',
                body: msg,
                sound: "default"
            }   
        };

        return admin.messaging().sendToDevice(tokens, payload);
    });
});

