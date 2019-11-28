Vue.component('loginofcomponent',{
    template:`
    <div>
    <button v-on:click="loginoff">Login: {{logIn}}</button>
<button v-on:click="adminoff">Admin:{{isAdmin}}</button>
<button v-on:click="stateChanges">State:{{logIn}} {{isAdmin}}</button>
</div>    
`,
    methods:{
        loginoff(){
            this.$parent.loginoff();
        },
        adminoff(){
            this.$parent.adminoff();
        },
        stateChanges(){
            console.log("plop");
            console.log(this.logIn +" " + this.isAdmin);
        }
    },
    props:['logIn','isAdmin']
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
        };
    }
});
Vue.component('menutitrecomponent',{
    template:`
    <div class="titre">
        <div v-if="isAdmin">
            <form class="ajoutPlat">
                <div class="platPicForm">
                    <label for="picture">Image: 
                    </label>
                   <input id="picture" type="text">
                 </div>
                <div class="nomPlatForm">
                    <label for="nom">Nom du plat:
                    </label>
                    <input id="nom" type="text">
                </div>
                <div class="descriptionPlatDiv">
                    <label for="description">Description: 
                    </label>
                    <input id="description" type="text">
                </div>
                <div class="prixPlat">
                    <label for="prix">Prix</label>
                    <input id="prix" type="number">
                </div>
                <button v-on:click.stop.prevent="saveNewDish">Enregistrer</button>
            </form>
        </div>
        <div v-else class="titre">
            <h1> La carte du jour </h1>
            <componentseparator></componentseparator>
        </div>
    </div>
    `,
    props:['isAdmin',"plats"],
    methods:{
        saveNewDish(){
            newId=Date.now();
            newDish={id:newId,nom:nom.value,picture:picture.value,description:description.value,prix:prix.value,nombre:0};
            console.log(newDish.id);
            this.$parent.plats.push(newDish);
        }
    }
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
            <div v-if="!isAdmin" class="commander">
                <FORM class="quantProd">
                  <SELECT size="1" v-model="plat.nombre">
                    <option>0</option>
                    <OPTION>1</option>
                    <OPTION>2</option>
                    <OPTION>3</option>
                    <OPTION>4</option>
                    <OPTION>5</option>
                   </SELECT>
                </FORM>                        
                <button class="commander">Commander {{plat.nombre}}*{{plat.prix}}</button>
            </div>
            <div v-else class="modifierplat">
            <form class="modifiePlatForm">
                 <div class="platPicForm">
                    <label for="picture">Image: </label>
                    <input id="picture" type="text" v-model="plat.picture">
                </div>
                <div class="nomPlatForm">
                    <label for="nom">Nom du plat:</label>
                    <input id="nom" type="text" v-model="plat.nom">
                </div> 
                <div class="descriptionPlatDiv">
                <label for="description">Description: </label>
                <input id="description" type="textarea" v-model="plat.description">
                </div>
                </form>
            <button class="validerChangePlat" v-on:click="validerChangePlat(plat.id)">Valider</button>
            <button class="supprimerPlat" v-on:click.stop.prevent="supprimerPlat(plat.id)">Supprimer</button>
            
            </div>
        </div>
    </div>
    `,
        props:['plat','isAdmin'],
        methods:{
            validerChangePlat(id){
                const plats=this.$parent.plats;
                const getPlat=plats.find(plat=>plat.id===id)
                console.log(description.value);
                plats[this.$parent.plats.indexOf(getPlat)]={id:getPlat,nom:nom.value,picture:picture.value,description:description.value,prix:prix.value,nombre:0};
                
            },
            supprimerPlat(id){  
                this.$parent.plats=this.$parent.plats.filter(plat=>plat.id !==id)
            }}
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
});
Vue.component('logincomponent',{
    template:`
    <div class="connexion">
    <h3>{{titre}} </h3>
    <form action="ln.jo@laposte.net" method="post"enctype="multipart/form-data">
    <h3>{{id}} </h3>
    <input type="text" size="30" name="login" maxlength="30">
    <h3>Mot de passe : </h3>
    <input type="text" size="30" name="password" maxlength="30">
    </form>
    <br>
    <button> Se connecter </button> <button> Créer un compte </button>
    </div>
    `,
    data:function(){
        return{
            titre:'Login Component',
            id:'Identifiant'
        };
    }
});
Vue.component('admincomponent',{
    template:`<div class="infosGerant">
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
         };
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
     };
 }
});
Vue.component('componentseparator',{
    template:`<span class="separator">***********************************</span>`
});
var app=new Vue({
    el:'#app',
    data:{
        logIn:false,
        isAdmin:true,
        counter:0,
        plats:[
            {id:Date.now(),nom:'Bongo’o tjobi', picture:'./photosPlats/Mbongo.jpg',description:' Poisson frais ou viande (au choix) assaisonné d\'un mélange d\'épices et sauce noire',prix:9,nombre:0},
            {id:Date.now()+1,nom:'Kpem',picture:'./photosPlats/Kpem.jpg', description:'Base de feuilles de manioc pilées, de jus de noix de palme et d\'aubergines africaines.',prix:10,nombre:0}
        ],
        days:[{id:1,nom:'Lundi', ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:2,nom:"Mardi", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:3,nom:"Mercredi", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:4,nom:"Jeudi", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:5,nom:"Vendredi", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:6,nom:"Samedi", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"},
              {id:7,nom:"Dimanche", ouvertureMatin:"10:00", fermetureMidi:"13:00", ouvertureSoir:"17:00", fermeturesoir:"22:00"}
            ],
        listeCommande:[]
    },
    methods:{
        loginoff(){
           
            this.logIn=!this.logIn;
            console.log("Login:"+this.logIn);
        },
        adminoff(){
            
            this.isAdmin=!this.isAdmin;
            console.log("Admin:"+this.isAdmin);
        }

    }
});