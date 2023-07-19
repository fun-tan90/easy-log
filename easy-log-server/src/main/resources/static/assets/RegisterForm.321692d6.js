var we=Object.defineProperty,Se=Object.defineProperties;var xe=Object.getOwnPropertyDescriptors;var Z=Object.getOwnPropertySymbols;var Te=Object.prototype.hasOwnProperty,De=Object.prototype.propertyIsEnumerable;var _=Math.pow,K=(r,e,t)=>e in r?we(r,e,{enumerable:!0,configurable:!0,writable:!0,value:t}):r[e]=t,S=(r,e)=>{for(var t in e||(e={}))Te.call(e,t)&&K(r,t,e[t]);if(Z)for(var t of Z(e))De.call(e,t)&&K(r,t,e[t]);return r},A=(r,e)=>Se(r,xe(e));var X=(r,e,t)=>new Promise((s,n)=>{var a=i=>{try{c(t.next(i))}catch(l){n(l)}},o=i=>{try{c(t.throw(i))}catch(l){n(l)}},c=i=>i.done?s(i.value):Promise.resolve(i.value).then(a,o);c((t=t.apply(r,e)).next())});import{u as Ie,a as Ae,L as Ce,_ as Le,b as Pe}from"./LoginFormTitle.5e351425.js";import{a as oe,I as j,O as N,w as F,b as _e,f as ie,Q as Re,x as je,R as Ne,k as d,S as Oe,o as z,h as ce,j as Ue,T as $e,U as Ge,H as E,V as Ye,W as Fe,X as ze,Y as Ve,l as le,i as J,q as O,Z as We,c as qe,D as Be,n as M,J as U,t as $,K as Q,M as He}from"./index.c2fb29d9.js";import{F as ee}from"./index.8c7aee6a.js";import"./index.140cefd0.js";import{a as Ze}from"./index.70703b19.js";import{C as Ke}from"./index.66476e28.js";import"./get.4aff8b30.js";import"./useSize.be05b693.js";const Xe=r=>Object.keys(r).length===0,V=(r,e)=>r.push.apply(r,e),Je=(r,e)=>r.split("").map(s=>e[s]||s).join(""),L=r=>r.sort((e,t)=>e.i-t.i||e.j-t.j),te=r=>{const e={};let t=1;return r.forEach(s=>{e[s]=t,t+=1}),e};var Qe={4:[[1,2],[2,3]],5:[[1,3],[2,3]],6:[[1,2],[2,4],[4,5]],7:[[1,3],[2,3],[4,5],[4,6]],8:[[2,4],[4,6]]};const se=2050,ne=1e3,et=Qe,tt=10,st=1e4,ue=10,he=50,de=20,fe=/^[A-Z\xbf-\xdf][^A-Z\xbf-\xdf]+$/,nt=/^[^A-Z\xbf-\xdf]+[A-Z\xbf-\xdf]$/,rt=/^[A-Z\xbf-\xdf]+$/,ge=/^[^a-z\xdf-\xff]+$/,at=/^[a-z\xdf-\xff]+$/,ot=/^[^A-Z\xbf-\xdf]+$/,it=/[a-z\xdf-\xff]/,ct=/[A-Z\xbf-\xdf]/,lt=/[^A-Za-z\xbf-\xdf]/gi,ut=/^\d+$/,q=new Date().getFullYear(),ht={recentYear:/19\d\d|200\d|201\d|202\d/g};class dt{match({password:e}){const t=[...this.getMatchesWithoutSeparator(e),...this.getMatchesWithSeparator(e)],s=this.filterNoise(t);return L(s)}getMatchesWithSeparator(e){const t=[],s=/^(\d{1,4})([\s/\\_.-])(\d{1,2})\2(\d{1,4})$/;for(let n=0;n<=Math.abs(e.length-6);n+=1)for(let a=n+5;a<=n+9&&!(a>=e.length);a+=1){const o=e.slice(n,+a+1||9e9),c=s.exec(o);if(c!=null){const i=this.mapIntegersToDayMonthYear([parseInt(c[1],10),parseInt(c[3],10),parseInt(c[4],10)]);i!=null&&t.push({pattern:"date",token:o,i:n,j:a,separator:c[2],year:i.year,month:i.month,day:i.day})}}return t}getMatchesWithoutSeparator(e){const t=[],s=/^\d{4,8}$/,n=a=>Math.abs(a.year-q);for(let a=0;a<=Math.abs(e.length-4);a+=1)for(let o=a+3;o<=a+7&&!(o>=e.length);o+=1){const c=e.slice(a,+o+1||9e9);if(s.exec(c)){const i=[],l=c.length;if(et[l].forEach(([f,y])=>{const b=this.mapIntegersToDayMonthYear([parseInt(c.slice(0,f),10),parseInt(c.slice(f,y),10),parseInt(c.slice(y),10)]);b!=null&&i.push(b)}),i.length>0){let f=i[0],y=n(i[0]);i.slice(1).forEach(b=>{const u=n(b);u<y&&(f=b,y=u)}),t.push({pattern:"date",token:c,i:a,j:o,separator:"",year:f.year,month:f.month,day:f.day})}}}return t}filterNoise(e){return e.filter(t=>{let s=!1;const n=e.length;for(let a=0;a<n;a+=1){const o=e[a];if(t!==o&&o.i<=t.i&&o.j>=t.j){s=!0;break}}return!s})}mapIntegersToDayMonthYear(e){if(e[1]>31||e[1]<=0)return null;let t=0,s=0,n=0;for(let a=0,o=e.length;a<o;a+=1){const c=e[a];if(c>99&&c<ne||c>se)return null;c>31&&(s+=1),c>12&&(t+=1),c<=0&&(n+=1)}return s>=2||t===3||n>=2?null:this.getDayMonth(e)}getDayMonth(e){const t=[[e[2],e.slice(0,2)],[e[0],e.slice(1,3)]],s=t.length;for(let n=0;n<s;n+=1){const[a,o]=t[n];if(ne<=a&&a<=se){const c=this.mapIntegersToDayMonth(o);return c!=null?{year:a,month:c.month,day:c.day}:null}}for(let n=0;n<s;n+=1){const[a,o]=t[n],c=this.mapIntegersToDayMonth(o);if(c!=null)return{year:this.twoToFourDigitYear(a),month:c.month,day:c.day}}return null}mapIntegersToDayMonth(e){const t=[e,e.slice().reverse()];for(let s=0;s<t.length;s+=1){const n=t[s],a=n[0],o=n[1];if(a>=1&&a<=31&&o>=1&&o<=12)return{day:a,month:o}}return null}twoToFourDigitYear(e){return e>99?e:e>50?e+1900:e+2e3}}const D=new Uint32Array(65536),ft=(r,e)=>{const t=r.length,s=e.length,n=1<<t-1;let a=-1,o=0,c=t,i=t;for(;i--;)D[r.charCodeAt(i)]|=1<<i;for(i=0;i<s;i++){let l=D[e.charCodeAt(i)];const g=l|o;l|=(l&a)+a^a,o|=~(l|a),a&=l,o&n&&c++,a&n&&c--,o=o<<1|1,a=a<<1|~(g|o),o&=g}for(i=t;i--;)D[r.charCodeAt(i)]=0;return c},gt=(r,e)=>{const t=r.length,s=e.length,n=[],a=[],o=Math.ceil(t/32),c=Math.ceil(s/32);let i=s;for(let u=0;u<o;u++)a[u]=-1,n[u]=0;let l=0;for(;l<c-1;l++){let u=0,p=-1;const I=l*32,x=Math.min(32,s)+I;for(let m=I;m<x;m++)D[e.charCodeAt(m)]|=1<<m;i=s;for(let m=0;m<t;m++){const T=D[r.charCodeAt(m)],v=a[m/32|0]>>>m&1,w=n[m/32|0]>>>m&1,B=T|u,H=((T|w)&p)+p^p|T|w;let P=u|~(H|p),R=p&H;P>>>31^v&&(a[m/32|0]^=1<<m),R>>>31^w&&(n[m/32|0]^=1<<m),P=P<<1|v,R=R<<1|w,p=R|~(B|P),u=P&B}for(let m=I;m<x;m++)D[e.charCodeAt(m)]=0}let g=0,f=-1;const y=l*32,b=Math.min(32,s-y)+y;for(let u=y;u<b;u++)D[e.charCodeAt(u)]|=1<<u;i=s;for(let u=0;u<t;u++){const p=D[r.charCodeAt(u)],I=a[u/32|0]>>>u&1,x=n[u/32|0]>>>u&1,m=p|g,T=((p|x)&f)+f^f|p|x;let v=g|~(T|f),w=f&T;i+=v>>>s-1&1,i-=w>>>s-1&1,v>>>31^I&&(a[u/32|0]^=1<<u),w>>>31^x&&(n[u/32|0]^=1<<u),v=v<<1|I,w=w<<1|x,f=w|~(m|v),g=v&m}for(let u=y;u<b;u++)D[e.charCodeAt(u)]=0;return i},pe=(r,e)=>{if(r.length>e.length){const t=e;e=r,r=t}return r.length===0?e.length:r.length<=32?ft(r,e):gt(r,e)},pt=(r,e)=>{let t=1/0,s=0;for(let n=0;n<e.length;n++){const a=pe(r,e[n]);a<t&&(t=a,s=n)}return e[s]};var mt={closest:pt,distance:pe};const yt=(r,e,t)=>{const s=r.length<=e.length,n=r.length<=t;return s||n?Math.ceil(r.length/4):t},bt=(r,e,t)=>{let s=0;const n=Object.keys(e).find(a=>{const o=yt(r,a,t),c=mt.distance(r,a),i=c<=o;return i&&(s=c),i});return n?{levenshteinDistance:s,levenshteinDistanceEntry:n}:{}};var Mt={a:["4","@"],b:["8"],c:["(","{","[","<"],e:["3"],g:["6","9"],i:["1","!","|"],l:["1","|","7"],o:["0"],s:["$","5"],t:["+","7"],x:["%"],z:["2"]},G={warnings:{straightRow:"straightRow",keyPattern:"keyPattern",simpleRepeat:"simpleRepeat",extendedRepeat:"extendedRepeat",sequences:"sequences",recentYears:"recentYears",dates:"dates",topTen:"topTen",topHundred:"topHundred",common:"common",similarToCommon:"similarToCommon",wordByItself:"wordByItself",namesByThemselves:"namesByThemselves",commonNames:"commonNames",userInputs:"userInputs",pwned:"pwned"},suggestions:{l33t:"l33t",reverseWords:"reverseWords",allUppercase:"allUppercase",capitalization:"capitalization",dates:"dates",recentYears:"recentYears",associatedYears:"associatedYears",sequences:"sequences",repeated:"repeated",longerKeyboardPattern:"longerKeyboardPattern",anotherWord:"anotherWord",useWords:"useWords",noNeed:"noNeed",pwned:"pwned"},timeEstimation:{ltSecond:"ltSecond",second:"second",seconds:"seconds",minute:"minute",minutes:"minutes",hour:"hour",hours:"hours",day:"day",days:"days",month:"month",months:"months",year:"year",years:"years",centuries:"centuries"}};class kt{constructor(){this.matchers={},this.l33tTable=Mt,this.dictionary={userInputs:[]},this.rankedDictionaries={},this.translations=G,this.graphs={},this.availableGraphs=[],this.useLevenshteinDistance=!1,this.levenshteinThreshold=2,this.setRankedDictionaries()}setOptions(e={}){e.l33tTable&&(this.l33tTable=e.l33tTable),e.dictionary&&(this.dictionary=e.dictionary,this.setRankedDictionaries()),e.translations&&this.setTranslations(e.translations),e.graphs&&(this.graphs=e.graphs),e.useLevenshteinDistance!==void 0&&(this.useLevenshteinDistance=e.useLevenshteinDistance),e.levenshteinThreshold!==void 0&&(this.levenshteinThreshold=e.levenshteinThreshold)}setTranslations(e){if(this.checkCustomTranslations(e))this.translations=e;else throw new Error("Invalid translations object fallback to keys")}checkCustomTranslations(e){let t=!0;return Object.keys(G).forEach(s=>{if(s in e){const n=s;Object.keys(G[n]).forEach(a=>{a in e[n]||(t=!1)})}else t=!1}),t}setRankedDictionaries(){const e={};Object.keys(this.dictionary).forEach(t=>{e[t]=this.getRankedDictionary(t)}),this.rankedDictionaries=e}getRankedDictionary(e){const t=this.dictionary[e];if(e==="userInputs"){const s=[];return t.forEach(n=>{const a=typeof n;(a==="string"||a==="number"||a==="boolean")&&s.push(n.toString().toLowerCase())}),te(s)}return te(t)}extendUserInputsDictionary(e){this.dictionary.userInputs?this.dictionary.userInputs=[...this.dictionary.userInputs,...e]:this.dictionary.userInputs=e,this.rankedDictionaries.userInputs=this.getRankedDictionary("userInputs")}addMatcher(e,t){this.matchers[e]?console.info("Matcher already exists"):this.matchers[e]=t}}const h=new kt;class vt{constructor(e){this.defaultMatch=e}match({password:e}){const t=e.split("").reverse().join("");return this.defaultMatch({password:t}).map(s=>A(S({},s),{token:s.token.split("").reverse().join(""),reversed:!0,i:e.length-1-s.j,j:e.length-1-s.i}))}}class Et{constructor(e){this.defaultMatch=e}match({password:e}){const t=[],s=this.enumerateL33tSubs(this.relevantL33tSubtable(e,h.l33tTable));for(let n=0;n<s.length;n+=1){const a=s[n];if(Xe(a))break;const o=Je(e,a);this.defaultMatch({password:o}).forEach(i=>{const l=e.slice(i.i,+i.j+1||9e9);if(l.toLowerCase()!==i.matchedWord){const g={};Object.keys(a).forEach(y=>{const b=a[y];l.indexOf(y)!==-1&&(g[y]=b)});const f=Object.keys(g).map(y=>`${y} -> ${g[y]}`).join(", ");t.push(A(S({},i),{l33t:!0,token:l,sub:g,subDisplay:f}))}})}return t.filter(n=>n.token.length>1)}relevantL33tSubtable(e,t){const s={},n={};return e.split("").forEach(a=>{s[a]=!0}),Object.keys(t).forEach(a=>{const c=t[a].filter(i=>i in s);c.length>0&&(n[a]=c)}),n}enumerateL33tSubs(e){const t=Object.keys(e);return this.getSubs(t,[[]],e).map(n=>{const a={};return n.forEach(([o,c])=>{a[o]=c}),a})}getSubs(e,t,s){if(!e.length)return t;const n=e[0],a=e.slice(1),o=[];s[n].forEach(i=>{t.forEach(l=>{let g=-1;for(let f=0;f<l.length;f+=1)if(l[f][0]===i){g=f;break}if(g===-1){const f=l.concat([[i,n]]);o.push(f)}else{const f=l.slice(0);f.splice(g,1),f.push([i,n]),o.push(l),o.push(f)}})});const c=this.dedup(o);return a.length?this.getSubs(a,c,s):c}dedup(e){const t=[],s={};return e.forEach(n=>{const a=n.map((c,i)=>[c,i]);a.sort();const o=a.map(([c,i])=>`${c},${i}`).join("-");o in s||(s[o]=!0,t.push(n))}),t}}class wt{constructor(){this.l33t=new Et(this.defaultMatch),this.reverse=new vt(this.defaultMatch)}match({password:e}){const t=[...this.defaultMatch({password:e}),...this.reverse.match({password:e}),...this.l33t.match({password:e})];return L(t)}defaultMatch({password:e}){const t=[],s=e.length,n=e.toLowerCase();return Object.keys(h.rankedDictionaries).forEach(a=>{const o=h.rankedDictionaries[a];for(let c=0;c<s;c+=1)for(let i=c;i<s;i+=1){const l=n.slice(c,+i+1||9e9),g=l in o;let f={};const y=c===0&&i===s-1;h.useLevenshteinDistance&&y&&!g&&(f=bt(l,o,h.levenshteinThreshold));const b=Object.keys(f).length!==0;if(g||b){const u=b?f.levenshteinDistanceEntry:l,p=o[u];t.push(S({pattern:"dictionary",i:c,j:i,token:e.slice(c,+i+1||9e9),matchedWord:l,rank:p,dictionaryName:a,reversed:!1,l33t:!1},f))}}}),t}}class St{match({password:e,regexes:t=ht}){const s=[];return Object.keys(t).forEach(n=>{const a=t[n];a.lastIndex=0;const o=a.exec(e);if(o){const c=o[0];s.push({pattern:"regex",token:c,i:o.index,j:o.index+o[0].length-1,regexName:n,regexMatch:o})}}),L(s)}}var C={nCk(r,e){let t=r;if(e>t)return 0;if(e===0)return 1;let s=1;for(let n=1;n<=e;n+=1)s*=t,s/=n,t-=1;return s},log10(r){return Math.log(r)/Math.log(10)},log2(r){return Math.log(r)/Math.log(2)},factorial(r){let e=1;for(let t=2;t<=r;t+=1)e*=t;return e}},xt=({token:r})=>{let e=_(tt,r.length);e===Number.POSITIVE_INFINITY&&(e=Number.MAX_VALUE);let t;return r.length===1?t=ue+1:t=he+1,Math.max(e,t)},Tt=({year:r,separator:e})=>{let s=Math.max(Math.abs(r-q),de)*365;return e&&(s*=4),s};const Dt=r=>{const e=r.split(""),t=e.filter(o=>o.match(it)).length,s=e.filter(o=>o.match(ct)).length;let n=0;const a=Math.min(t,s);for(let o=1;o<=a;o+=1)n+=C.nCk(t+s,o);return n};var It=r=>{const e=r.replace(lt,"");if(e.match(ot)||e.toLowerCase()===e)return 1;const t=[fe,nt,ge],s=t.length;for(let n=0;n<s;n+=1){const a=t[n];if(e.match(a))return 2}return Dt(e)};const At=({subs:r,subbed:e,token:t})=>{const s=r[e],n=t.toLowerCase().split(""),a=n.filter(c=>c===e).length,o=n.filter(c=>c===s).length;return{subbedCount:a,unsubbedCount:o}};var Ct=({l33t:r,sub:e,token:t})=>{if(!r)return 1;let s=1;const n=e;return Object.keys(n).forEach(a=>{const{subbedCount:o,unsubbedCount:c}=At({subs:n,subbed:a,token:t});if(o===0||c===0)s*=2;else{const i=Math.min(c,o);let l=0;for(let g=1;g<=i;g+=1)l+=C.nCk(c+o,g);s*=l}}),s},Lt=({rank:r,reversed:e,l33t:t,sub:s,token:n})=>{const a=r,o=It(n),c=Ct({l33t:t,sub:s,token:n}),i=e&&2||1,l=a*o*c*i;return{baseGuesses:a,uppercaseVariations:o,l33tVariations:c,calculation:l}},Pt=({regexName:r,regexMatch:e,token:t})=>{const s={alphaLower:26,alphaUpper:26,alpha:52,alphanumeric:62,digits:10,symbols:33};if(r in s)return _(s[r],t.length);switch(r){case"recentYear":return Math.max(Math.abs(parseInt(e[0],10)-q),de)}return 0},_t=({baseGuesses:r,repeatCount:e})=>r*e,Rt=({token:r,ascending:e})=>{const t=r.charAt(0);let s=0;return["a","A","z","Z","0","1","9"].includes(t)?s=4:t.match(/\d/)?s=10:s=26,e||(s*=2),s*r.length};const jt=r=>{let e=0;return Object.keys(r).forEach(t=>{e+=r[t].filter(n=>!!n).length}),e/=Object.entries(r).length,e},Nt=({token:r,graph:e,turns:t})=>{const s=Object.keys(h.graphs[e]).length,n=jt(h.graphs[e]);let a=0;const o=r.length;for(let c=2;c<=o;c+=1){const i=Math.min(t,c-1);for(let l=1;l<=i;l+=1)a+=C.nCk(c-1,l-1)*s*_(n,l)}return a};var Ot=({graph:r,token:e,shiftedCount:t,turns:s})=>{let n=Nt({token:e,graph:r,turns:s});if(t){const a=e.length-t;if(t===0||a===0)n*=2;else{let o=0;for(let c=1;c<=Math.min(t,a);c+=1)o+=C.nCk(t+a,c);n*=o}}return Math.round(n)};const Ut=(r,e)=>{let t=1;return r.token.length<e.length&&(r.token.length===1?t=ue:t=he),t},re={bruteforce:xt,date:Tt,dictionary:Lt,regex:Pt,repeat:_t,sequence:Rt,spatial:Ot},$t=(r,e)=>re[r]?re[r](e):h.matchers[r]&&"scoring"in h.matchers[r]?h.matchers[r].scoring(e):0;var Gt=(r,e)=>{const t={};if("guesses"in r&&r.guesses!=null)return r;const s=Ut(r,e),n=$t(r.pattern,r);let a=0;typeof n=="number"?a=n:r.pattern==="dictionary"&&(a=n.calculation,t.baseGuesses=n.baseGuesses,t.uppercaseVariations=n.uppercaseVariations,t.l33tVariations=n.l33tVariations);const o=Math.max(a,s);return A(S(S({},r),t),{guesses:o,guessesLog10:C.log10(o)})};const k={password:"",optimal:{},excludeAdditive:!1,fillArray(r,e){const t=[];for(let s=0;s<r;s+=1){let n=[];e==="object"&&(n={}),t.push(n)}return t},makeBruteforceMatch(r,e){return{pattern:"bruteforce",token:this.password.slice(r,+e+1||9e9),i:r,j:e}},update(r,e){const t=r.j,s=Gt(r,this.password);let n=s.guesses;e>1&&(n*=this.optimal.pi[s.i-1][e-1]);let a=C.factorial(e)*n;this.excludeAdditive||(a+=_(st,e-1));let o=!1;Object.keys(this.optimal.g[t]).forEach(c=>{const i=this.optimal.g[t][c];parseInt(c,10)<=e&&i<=a&&(o=!0)}),o||(this.optimal.g[t][e]=a,this.optimal.m[t][e]=s,this.optimal.pi[t][e]=n)},bruteforceUpdate(r){let e=this.makeBruteforceMatch(0,r);this.update(e,1);for(let t=1;t<=r;t+=1){e=this.makeBruteforceMatch(t,r);const s=this.optimal.m[t-1];Object.keys(s).forEach(n=>{s[n].pattern!=="bruteforce"&&this.update(e,parseInt(n,10)+1)})}},unwind(r){const e=[];let t=r-1,s=0,n=1/0;const a=this.optimal.g[t];for(a&&Object.keys(a).forEach(o=>{const c=a[o];c<n&&(s=parseInt(o,10),n=c)});t>=0;){const o=this.optimal.m[t][s];e.unshift(o),t=o.i-1,s-=1}return e}};var W={mostGuessableMatchSequence(r,e,t=!1){k.password=r,k.excludeAdditive=t;const s=r.length;let n=k.fillArray(s,"array");e.forEach(i=>{n[i.j].push(i)}),n=n.map(i=>i.sort((l,g)=>l.i-g.i)),k.optimal={m:k.fillArray(s,"object"),pi:k.fillArray(s,"object"),g:k.fillArray(s,"object")};for(let i=0;i<s;i+=1)n[i].forEach(l=>{l.i>0?Object.keys(k.optimal.m[l.i-1]).forEach(g=>{k.update(l,parseInt(g,10)+1)}):k.update(l,1)}),k.bruteforceUpdate(i);const a=k.unwind(s),o=a.length,c=this.getGuesses(r,o);return{password:r,guesses:c,guessesLog10:C.log10(c),sequence:a}},getGuesses(r,e){const t=r.length;let s=0;return r.length===0?s=1:s=k.optimal.g[t-1][e],s}};class Yt{match({password:e,omniMatch:t}){const s=[];let n=0;for(;n<e.length;){const o=this.getGreedyMatch(e,n),c=this.getLazyMatch(e,n);if(o==null)break;const{match:i,baseToken:l}=this.setMatchToken(o,c);if(i){const g=i.index+i[0].length-1,f=this.getBaseGuesses(l,t);s.push(this.normalizeMatch(l,g,i,f)),n=g+1}}return s.some(o=>o instanceof Promise)?Promise.all(s):s}normalizeMatch(e,t,s,n){const a={pattern:"repeat",i:s.index,j:t,token:s[0],baseToken:e,baseGuesses:0,repeatCount:s[0].length/e.length};return n instanceof Promise?n.then(o=>A(S({},a),{baseGuesses:o})):A(S({},a),{baseGuesses:n})}getGreedyMatch(e,t){const s=/(.+)\1+/g;return s.lastIndex=t,s.exec(e)}getLazyMatch(e,t){const s=/(.+?)\1+/g;return s.lastIndex=t,s.exec(e)}setMatchToken(e,t){const s=/^(.+?)\1+$/;let n,a="";if(t&&e[0].length>t[0].length){n=e;const o=s.exec(n[0]);o&&(a=o[1])}else n=t,n&&(a=n[1]);return{match:n,baseToken:a}}getBaseGuesses(e,t){const s=t.match(e);return s instanceof Promise?s.then(a=>W.mostGuessableMatchSequence(e,a).guesses):W.mostGuessableMatchSequence(e,s).guesses}}class Ft{constructor(){this.MAX_DELTA=5}match({password:e}){const t=[];if(e.length===1)return[];let s=0,n=null;const a=e.length;for(let o=1;o<a;o+=1){const c=e.charCodeAt(o)-e.charCodeAt(o-1);if(n==null&&(n=c),c!==n){const i=o-1;this.update({i:s,j:i,delta:n,password:e,result:t}),s=i,n=c}}return this.update({i:s,j:a-1,delta:n,password:e,result:t}),t}update({i:e,j:t,delta:s,password:n,result:a}){if(t-e>1||Math.abs(s)===1){const o=Math.abs(s);if(o>0&&o<=this.MAX_DELTA){const c=n.slice(e,+t+1||9e9),{sequenceName:i,sequenceSpace:l}=this.getSequence(c);return a.push({pattern:"sequence",i:e,j:t,token:n.slice(e,+t+1||9e9),sequenceName:i,sequenceSpace:l,ascending:s>0})}}return null}getSequence(e){let t="unicode",s=26;return at.test(e)?(t="lower",s=26):rt.test(e)?(t="upper",s=26):ut.test(e)&&(t="digits",s=10),{sequenceName:t,sequenceSpace:s}}}class zt{constructor(){this.SHIFTED_RX=/[~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:"ZXCVBNM<>?]/}match({password:e}){const t=[];return Object.keys(h.graphs).forEach(s=>{const n=h.graphs[s];V(t,this.helper(e,n,s))}),L(t)}checkIfShifted(e,t,s){return!e.includes("keypad")&&this.SHIFTED_RX.test(t.charAt(s))?1:0}helper(e,t,s){let n;const a=[];let o=0;const c=e.length;for(;o<c-1;){let i=o+1,l=0,g=0;for(n=this.checkIfShifted(s,e,o);;){const f=e.charAt(i-1),y=t[f]||[];let b=!1,u=-1,p=-1;if(i<c){const I=e.charAt(i),x=y.length;for(let m=0;m<x;m+=1){const T=y[m];if(p+=1,T){const v=T.indexOf(I);if(v!==-1){b=!0,u=p,v===1&&(n+=1),l!==u&&(g+=1,l=u);break}}}}if(b)i+=1;else{i-o>2&&a.push({pattern:"spatial",i:o,j:i-1,token:e.slice(o,i),graph:s,turns:g,shiftedCount:n}),o=i;break}}}return a}}class Vt{constructor(){this.matchers={date:dt,dictionary:wt,regex:St,repeat:Yt,sequence:Ft,spatial:zt}}match(e){const t=[],s=[];return[...Object.keys(this.matchers),...Object.keys(h.matchers)].forEach(a=>{if(!this.matchers[a]&&!h.matchers[a])return;const o=this.matchers[a]?this.matchers[a]:h.matchers[a].Matching,i=new o().match({password:e,omniMatch:this});i instanceof Promise?(i.then(l=>{V(t,l)}),s.push(i)):V(t,i)}),s.length>0?new Promise(a=>{Promise.all(s).then(()=>{a(L(t))})}):L(t)}}const me=1,ye=me*60,be=ye*60,Me=be*24,ke=Me*31,ve=ke*12,Wt=ve*100,Y={second:me,minute:ye,hour:be,day:Me,month:ke,year:ve,century:Wt};class qt{translate(e,t){let s=e;t!==void 0&&t!==1&&(s+="s");const{timeEstimation:n}=h.translations;return n[s].replace("{base}",`${t}`)}estimateAttackTimes(e){const t={onlineThrottling100PerHour:e/.027777777777777776,onlineNoThrottling10PerSecond:e/10,offlineSlowHashing1e4PerSecond:e/1e4,offlineFastHashing1e10PerSecond:e/1e10},s={onlineThrottling100PerHour:"",onlineNoThrottling10PerSecond:"",offlineSlowHashing1e4PerSecond:"",offlineFastHashing1e10PerSecond:""};return Object.keys(t).forEach(n=>{const a=t[n];s[n]=this.displayTime(a)}),{crackTimesSeconds:t,crackTimesDisplay:s,score:this.guessesToScore(e)}}guessesToScore(e){return e<1e3+5?0:e<1e6+5?1:e<1e8+5?2:e<1e10+5?3:4}displayTime(e){let t="centuries",s;const n=Object.keys(Y),a=n.findIndex(o=>e<Y[o]);return a>-1&&(t=n[a-1],a!==0?s=Math.round(e/Y[t]):t="ltSecond"),this.translate(t,s)}}var Bt=()=>null,Ht=()=>({warning:h.translations.warnings.dates,suggestions:[h.translations.suggestions.dates]});const Zt=(r,e)=>{let t="";return e&&!r.l33t&&!r.reversed?r.rank<=10?t=h.translations.warnings.topTen:r.rank<=100?t=h.translations.warnings.topHundred:t=h.translations.warnings.common:r.guessesLog10<=4&&(t=h.translations.warnings.similarToCommon),t},Kt=(r,e)=>{let t="";return e&&(t=h.translations.warnings.wordByItself),t},Xt=(r,e)=>e?h.translations.warnings.namesByThemselves:h.translations.warnings.commonNames,Jt=(r,e)=>{let t="";const s=r.dictionaryName,n=s==="lastnames"||s.toLowerCase().includes("firstnames");return s==="passwords"?t=Zt(r,e):s.includes("wikipedia")?t=Kt(r,e):n?t=Xt(r,e):s==="userInputs"&&(t=h.translations.warnings.userInputs),t};var Qt=(r,e)=>{const t=Jt(r,e),s=[],n=r.token;return n.match(fe)?s.push(h.translations.suggestions.capitalization):n.match(ge)&&n.toLowerCase()!==n&&s.push(h.translations.suggestions.allUppercase),r.reversed&&r.token.length>=4&&s.push(h.translations.suggestions.reverseWords),r.l33t&&s.push(h.translations.suggestions.l33t),{warning:t,suggestions:s}},es=r=>r.regexName==="recentYear"?{warning:h.translations.warnings.recentYears,suggestions:[h.translations.suggestions.recentYears,h.translations.suggestions.associatedYears]}:{warning:"",suggestions:[]},ts=r=>{let e=h.translations.warnings.extendedRepeat;return r.baseToken.length===1&&(e=h.translations.warnings.simpleRepeat),{warning:e,suggestions:[h.translations.suggestions.repeated]}},ss=()=>({warning:h.translations.warnings.sequences,suggestions:[h.translations.suggestions.sequences]}),ns=r=>{let e=h.translations.warnings.keyPattern;return r.turns===1&&(e=h.translations.warnings.straightRow),{warning:e,suggestions:[h.translations.suggestions.longerKeyboardPattern]}};const ae={warning:"",suggestions:[]};class rs{constructor(){this.matchers={bruteforce:Bt,date:Ht,dictionary:Qt,regex:es,repeat:ts,sequence:ss,spatial:ns},this.defaultFeedback={warning:"",suggestions:[]},this.setDefaultSuggestions()}setDefaultSuggestions(){this.defaultFeedback.suggestions.push(h.translations.suggestions.useWords,h.translations.suggestions.noNeed)}getFeedback(e,t){if(t.length===0)return this.defaultFeedback;if(e>2)return ae;const s=h.translations.suggestions.anotherWord,n=this.getLongestMatch(t);let a=this.getMatchFeedback(n,t.length===1);return a!=null?(a.suggestions.unshift(s),a.warning==null&&(a.warning="")):a={warning:"",suggestions:[s]},a}getLongestMatch(e){let t=e[0];return e.slice(1).forEach(n=>{n.token.length>t.token.length&&(t=n)}),t}getMatchFeedback(e,t){return this.matchers[e.pattern]?this.matchers[e.pattern](e,t):h.matchers[e.pattern]&&"feedback"in h.matchers[e.pattern]?h.matchers[e.pattern].feedback(e,t):ae}}const Ee=()=>new Date().getTime(),as=(r,e,t)=>{const s=new rs,n=new qt,a=W.mostGuessableMatchSequence(e,r),o=Ee()-t,c=n.estimateAttackTimes(a.guesses);return A(S(S({calcTime:o},a),c),{feedback:s.getFeedback(c.score,a.sequence)})},os=(r,e)=>(e&&h.extendUserInputsDictionary(e),new Vt().match(r)),is=(r,e)=>{const t=Ee(),s=os(r,e);if(s instanceof Promise)throw new Error("You are using a Promised matcher, please use `zxcvbnAsync` for it.");return as(s,r,t)};const cs=oe({name:"StrengthMeter",components:{InputPassword:j.Password},props:{value:N.string,showInput:N.bool.def(!0),disabled:N.bool},emits:["score-change","change"],setup(r,{emit:e}){const t=F(""),{prefixCls:s}=_e("strength-meter"),n=ie(()=>{const{disabled:o}=r;if(o)return-1;const i=d(t)?is(d(t)).score:-1;return e("score-change",i),i});function a(o){t.value=o.target.value}return Re(()=>{t.value=r.value||""}),je(()=>d(t),o=>{e("change",o)}),{getPasswordStrength:n,handleChange:a,prefixCls:s,innerValueRef:t}}}),ls=["data-score"];function us(r,e,t,s,n,a){const o=Oe("InputPassword");return z(),ce("div",{class:O([r.prefixCls,"relative"])},[r.showInput?(z(),Ue(o,Ve({key:0},r.$attrs,{allowClear:"",value:r.innerValueRef,onChange:r.handleChange,disabled:r.disabled}),$e({_:2},[Ge(Object.keys(r.$slots),c=>({name:c,fn:E(i=>[Ye(r.$slots,c,Fe(ze(i||{})),void 0,!0)])}))]),1040,["value","onChange","disabled"])):le("",!0),J("div",{class:O(`${r.prefixCls}-bar`)},[J("div",{class:O(`${r.prefixCls}-bar--fill`),"data-score":r.getPasswordStrength},null,10,ls)],2)],2)}var hs=Ne(cs,[["render",us],["__scopeId","data-v-07feefbe"]]);const ds=We(hs),Es=oe({setup(r){const e=ee.Item,t=j.Password,{t:s}=qe(),{handleBackLogin:n,getLoginState:a}=Ie(),o=F(),c=F(!1),i=Be({account:"",password:"",confirmPassword:"",mobile:"",sms:"",policy:!1}),{getFormRules:l}=Ae(i),{validForm:g}=Pe(o),f=ie(()=>d(a)===Ce.REGISTER);function y(){return X(this,null,function*(){const b=yield g()})}return(b,u)=>d(f)?(z(),ce(He,{key:0},[M(Le,{class:"enter-x"}),M(d(ee),{class:"p-4 enter-x",model:d(i),rules:d(l),ref_key:"formRef",ref:o},{default:E(()=>[M(d(e),{name:"account",class:"enter-x"},{default:E(()=>[M(d(j),{class:"fix-auto-fill",size:"large",value:d(i).account,"onUpdate:value":u[0]||(u[0]=p=>d(i).account=p),placeholder:d(s)("sys.login.userName")},null,8,["value","placeholder"])]),_:1}),M(d(e),{name:"mobile",class:"enter-x"},{default:E(()=>[M(d(j),{size:"large",value:d(i).mobile,"onUpdate:value":u[1]||(u[1]=p=>d(i).mobile=p),placeholder:d(s)("sys.login.mobile"),class:"fix-auto-fill"},null,8,["value","placeholder"])]),_:1}),M(d(e),{name:"sms",class:"enter-x"},{default:E(()=>[M(d(Ke),{size:"large",class:"fix-auto-fill",value:d(i).sms,"onUpdate:value":u[2]||(u[2]=p=>d(i).sms=p),placeholder:d(s)("sys.login.smsCode")},null,8,["value","placeholder"])]),_:1}),M(d(e),{name:"password",class:"enter-x"},{default:E(()=>[M(d(ds),{size:"large",value:d(i).password,"onUpdate:value":u[3]||(u[3]=p=>d(i).password=p),placeholder:d(s)("sys.login.password")},null,8,["value","placeholder"])]),_:1}),M(d(e),{name:"confirmPassword",class:"enter-x"},{default:E(()=>[M(d(t),{size:"large",visibilityToggle:"",value:d(i).confirmPassword,"onUpdate:value":u[4]||(u[4]=p=>d(i).confirmPassword=p),placeholder:d(s)("sys.login.confirmPassword")},null,8,["value","placeholder"])]),_:1}),M(d(e),{class:"enter-x",name:"policy"},{default:E(()=>[M(d(Ze),{checked:d(i).policy,"onUpdate:checked":u[5]||(u[5]=p=>d(i).policy=p),size:"small"},{default:E(()=>[U($(d(s)("sys.login.policy")),1)]),_:1},8,["checked"])]),_:1}),M(d(Q),{type:"primary",class:"enter-x",size:"large",block:"",onClick:y,loading:c.value},{default:E(()=>[U($(d(s)("sys.login.registerButton")),1)]),_:1},8,["loading"]),M(d(Q),{size:"large",block:"",class:"mt-4 enter-x",onClick:d(n)},{default:E(()=>[U($(d(s)("sys.login.backSignIn")),1)]),_:1},8,["onClick"])]),_:1},8,["model","rules"])],64)):le("",!0)}});export{Es as default};
