import{R as i,a as r,bq as l,b as c,S as d,o as s,h as o,M as _,U as u,q as a,j as f,H as m,i as n,cV as y,cW as v}from"./index.c2fb29d9.js";const C=r({name:"MenuTypePicker",components:{Tooltip:l},props:{menuTypeList:{type:Array,defualt:()=>[]},handler:{type:Function,default:()=>({})},def:{type:String,default:""}},setup(){const{prefixCls:e}=c("setting-menu-type-picker");return{prefixCls:e}}}),h=e=>(y("data-v-2861cd82"),e=e(),v(),e),k=["onClick"],$=h(()=>n("div",{class:"mix-sidebar"},null,-1)),T=[$];function g(e,S,b,x,B,I){const p=d("Tooltip");return s(),o("div",{class:a(e.prefixCls)},[(s(!0),o(_,null,u(e.menuTypeList||[],t=>(s(),f(p,{key:t.title,title:t.title,placement:"bottom"},{default:m(()=>[n("div",{onClick:L=>e.handler(t),class:a([`${e.prefixCls}__item`,`${e.prefixCls}__item--${t.type}`,{[`${e.prefixCls}__item--active`]:e.def===t.type}])},T,10,k)]),_:2},1032,["title"]))),128))],2)}var q=i(C,[["render",g],["__scopeId","data-v-2861cd82"]]);export{q as default};