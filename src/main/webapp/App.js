/**
 * 
 */Vue.component('loginofcomponent',{
    template:`
    <div>
    
    <button  v-if="!this.$parent.logIn" v-on:click="connexionTool"> Login </button>
    <button v-if="this.$parent.logIn" v-on:click="toggle" > log out </button>
    <button v-on:click="adminoff">Admin:{{isAdmin}}</button>
<button v-on:clik="stateChanges">State:{{logIn}} {{isAdmin}}</button>
</div>    
`,
    methods:{
    	toggle(){
    		this.$parent.logOff();
    	},
    	connexionTool(){
    		console.log("ici fuckers  ????!!!! ", )
            this.$parent.connexionTool();
        },
        adminoff(){
            this.$parent.adminoff();
        },
        stateChanges(){
            console.log("plop");
            console.log(this.logIn +" " + this.isAdmin)
        }
    },
    props:['logIn','isAdmin', 'connexion']
});

Vue.component('containercomponent',{
    template:`
    <div class="container">
    <h1> {{titre}} </h1>
    <span class="accroche">{{accroche}}</span> 
    <p class="containerText">{{containertext}}</p>
    <p class="containerText">{{containertext2}}</p>
    </div>`,
    data:function(){
        return{
            titre:"LE K Méroun",
            accroche:'Manger africain sur le pouce, c\'est possible à Toulouse.',
            containertext:"Le K Méroun vous concocte des plats épicés à partir de produits locaux et livrés d'Afrique.",
            containertext2:"Commandez sur place ou à emporter"
        }
    }
});
Vue.component('menutitrecomponent',{
    template:`<div class="titre">
    <h1> La carte du jour </h1>
    <componentseparator></componentseparator>
    </div>`
});
Vue.component('platcomponent',{
    template:`
    <div class="element">
    <div class="image">
    <img v-bind:src="plat.picture" v-bind:alt="plat.nom">
    </div>
    <div class="platText">
       <h2 class="nomPlat"> {{plat.nom}}</h2>
       <p class="description">{{plat.description}}</p>
    </div>
    </div>
    `,
    props:['plat'],
    mounted(){
    	fetch('urlgetplat')
    	.then(res=> res.json())
    	.then((res)=>{
    		console.log(resw)
    	})
    }
});
Vue.component('componenthoraires',{
    template:`
  <div class="day">
  <span> {{day.nom}}</span>
  <span>{{day.ouvertureMatin}}-{{day.fermetureMidi}}</span>
  <span>{{day.ouvertureSoir}}-{{day.fermeturesoir}}</span>
  </div> 
`,
props:['day']

});
Vue.component('componentchangehoraires',{
    template:`
    <div class="day">
    <div class="nameDay">
    {{day.nom}}
    </div>
    <div class="matin">Matin:
    <input class="time" type="text" maxlength="5" placeholder="00:00">-<input class="time" type="text" maxlength="5" placeholder="00:00">
    </div>
    <div class="soir">
    Soir:
    <input class="time" type="text" maxlength="5" placeholder="00:00" v-model=day.ouvertureSoir>-<input class="time" type="text" maxlength="5" placeholder="00:00">
    </div>
    </div>
    `,
    props:['day']
})
Vue.component('logincomponent',{
    template:`
    <div class="connexion">
    <h3>{{titre}} </h3>
    <form v-on:submit.prevent="login" v-if="!signIn" method="post" enctype="multipart/form-data">
    <h3>{{id}} </h3>
    <input type="text" size="30" v-model="username" name="username" maxlength="30">
    <h3>Mot de passe : </h3>
    <input type="password" size="30" v-model="password" name="password" maxlength="30" />
     <input type="submit"  value="se connecter"/> 
    </form>
     <form v-on:submit.prevent="register" v-if="signIn" method="post"enctype="multipart/form-data">
    <h3> Votre nom d'utilisateur </h3>
    <input type="text" size="30" v-model="username" name="username" maxlength="30" />     
    <h3> Votre nom </h3>
    <input type="text" size="30" v-model="firstname" name="firstname" maxlength="30"/>
    <h3> Votre prénom </h3>
    <input type="text" size="30" v-model="lastname" name="lastname" maxlength="30" />
    <h3> Mot de passe : </h3>
    <input type="password" size="30"  v-model="password" name="password" maxlength="30" />
     <h3> confirmer le mot de passe : </h3>
    <input type="password" size="30" v-model="confirmPass" name="confirmPass" maxlength="30" />
    <input type="submit" value="s'inscrire"/>
    </form>
    <br>
  
    <button @click="toggle"> {{signIn ? "Se connecter" : "Créer un compte" }}  </button>
    </div>
    `,
    data:function(){
        return{
            titre:'Login Component',
            id:'Identifiant',
            signIn: false,
            username:"",
            firstname: "",
            lastname:"",
            password: "",
            confirmPass: "",
        }
    },
    methods:{
    	toggle(){
    		this.signIn= !this.signIn
    	},
    	login(){    		
    		fetch('/kmeroun/login',{
    			method: "POST",
    			headers: new Headers({
    				"Content-Type": "application/json"
    			}),
    			body: JSON.stringify({
    				Username : this.Username,
    				password : this.password
    			})
    		})
    		.then((res)=>{
    				if(!res.ok){
    					console.log(res)
    					return alert("Error occured during login process")
    				}
    				return res.json()
    			})
    			.then((response) =>{
    				console.log(response)
    				this.$parent.logOff();
    				this.$parent.connexionTool();
    			})
    	},
    	register(){
    		if(this.password == this.confirmPass && (this.password.length > 3)){
    			console.log(this.username, this.firstname, this.lastname, this.password)
    			fetch('/kmeroun/register',{
    				method: "POST",
    				headers: new Headers({
    					"Content-Type": "application/json"
    				}),
    				body: JSON.stringify({
    					username: this.username, 
    					firstname: this.firstname, 
    					lastname: this.lastname, 
    					password: this.password
    				})
    				
    			})
    			.then((res)=>{
    				if(!res.ok){
    					console.log(res)
    					return alert("Error occured during register process")
    				}
    				return res.json()
    			})
    			.then((response) =>{
    				console.log(response)
    				this.signIn = false
    			})
    		}else{
    			alert("Les mots de passes doivent être identiques et doivent comporter au minimum 4 lettres ou chiffres")
    		}
    	}
    	
    },
    props: ['logIn']
});
Vue.component('admincomponent',{
    template:
   `<div class="infosGerant">
    <h3>Bienvenue!</h3>
    </div>`
});
Vue.component('coordoneescomponent',{
    template:`<div class="coordonees">
    <h3 class="nomResto"> {{nomResto}} </h3>
     <span class="adresse">{{adresse}}</span>
     <span class="codePostalVille"> {{codePostalVille}}</span>
     <span class="phone"> Tel : {{phone}}</span>
     <span class="email"> @ :  <a href="mailto:Kmeroun@gmail.com">{{email}}</a></span>
     <button class="buttonFacebook"><a href="https://fr-fr.facebook.com/kmerounrestaurant/"> Facebook</a> </button></div>`,
     data:function(){
         return{
             nomResto:"Le K-Méroun",
             adresse:"5 avenue de l'autre monde",
             codePostalVille:"31000 Toulouse",
             phone:"05 51 49 26 53",
             email:"Kmeroun@gmail.com",
             facebook:'https://fr-fr.facebook.com/kmerounrestaurant/'
         }
     }
});

Vue.component('commandecomponent',{
    template:`<div class="commande">
    <h3 class="titreCommande">Votre commande:</h3>
<div class="ticket">
    <div class="recapClient">
      <p class="infoClient">Nom: {{nomClient}} </p>
      <p class="infoClient">Prénom: {{prenomClient}} </p>
      <p class="num commande">Numéro commande: <span class="comNumber">{{numCommande}}</span> </p>
    </div>
    <componentseparator></componentseparator>
      <ul>
      <li v-for="commandItem in commandItems" v-bind:key="commandItem.id" v-bind:commandItem='commandItem'> 
      <button class="cube">- </button> 1 
      <button class="cube">+</button>{{commandItem.nomPlat}} : {{commandItem.prix}} € 
        </li>
      
    </ul>
    <span class="total">Total : {{total}}€</span>
    <button class="reglement"> Regler la commande </button>
    </div>
 </div>`,
 data:function(){
     return{
         nomClient:"Donald",
         prenomClient:"Marc",
         numCommande:"13874B",
         commandItems:[{id:1,nomPlat:"Bongo'o tjobi", prix:9},
         {id:2,nomPlat:"Rataplaf", prix:10}],
         total:19
     }
 }
});
Vue.component('componentseparator',{
    template:`<span class="separator">***********************************</span>`
});
Vue.component('menutitrecomponent',{
    template:`
    <div>
    <div v-if="isAdmin">
        <form class="ajoutPlat">
            <div class="platPicForm">
                <label for="platPic">Image: 
                </label>
                <input id="platPic" type="text" v-model="plat.picture">
            </div>
            <div class="nomPlatForm">
                <label for="nomPlat">Nom du plat:
                </label>
                <input id="nomPlat" type="text" v-model="plat.nom">
            </div>
            <div class="descriptionPlatDiv">
                <label for="descriptionPlat">Description:
                </label>
                <input id="descriptionPlat" type="textarea" v-model="plat.description">
            </div>
            <button class="goAjoutPlat" v-on:click="goAjoutPlat">Valider</button>
        </form>
    </div>
        <div v-else class="titre">
            <h1> La carte du jour </h1>
            <componentseparator></componentseparator>
        </div>
        </div>
    `,
    props:['isAdmin'],
    methods:{
    	goAjoutPlat(){
    		fetch('url',{
    			method: "POST"
    		}),
    		body=JSON.stringify()
    		.then(res => res.json())
    		.then((response)=>{
    			console.log(response)
    		})
    	}
    }
});
var app=new Vue({
    el:'#app',
    data:{
    	connexion: false,
        logIn:false,
        isAdmin:false,
        counter:0,
        plats:[
            {id:1,nom:'Bongo’o tjobi', picture:'./photosPlats/Mbongo.jpg',description:' Poisson frais ou viande (au choix) assaisonné d\'un mélange d\'épices et sauce noire'},
            {id:2,nom:'Kpem',picture:'./photosPlats/Kpem.jpg', description:'Base de feuilles de manioc pilées, de jus de noix de palme et d\'aubergines africaines.'}
        ],
        days:[{id:1,nom:'Lundi', ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:2,nom:"Mardi", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:3,nom:"Mercredi", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:4,nom:"Jeudi", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:5,nom:"Vendredi", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:6,nom:"Samedi", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:7,nom:"Dimanche", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"}
            ]
    },
    methods:{
    	logOff(){
    		this.logIn = !this.logIn
    	},
        connexionTool(){
            this.connexion=!this.connexion;
        },
        adminoff(){
            this.isAdmin=!this.isAdmin;
            console.log("Admin:"+this.isAdmin);  
        }
    }
});