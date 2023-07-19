import{a as $,$ as H,w,x as Q,a0 as R,a1 as X,_ as c,a2 as q,z as b,n as x,a3 as E,a4 as z,r as J,v as L,a5 as W,Q as Y,a6 as Z,a7 as ee,a8 as ae}from"./index.c2fb29d9.js";var ne=globalThis&&globalThis.__rest||function(o,e){var u={};for(var a in o)Object.prototype.hasOwnProperty.call(o,a)&&e.indexOf(a)<0&&(u[a]=o[a]);if(o!=null&&typeof Object.getOwnPropertySymbols=="function")for(var t=0,a=Object.getOwnPropertySymbols(o);t<a.length;t++)e.indexOf(a[t])<0&&Object.prototype.propertyIsEnumerable.call(o,a[t])&&(u[a[t]]=o[a[t]]);return u},te={prefixCls:String,name:String,id:String,type:String,defaultChecked:{type:[Boolean,Number],default:void 0},checked:{type:[Boolean,Number],default:void 0},disabled:Boolean,tabindex:{type:[Number,String]},readonly:Boolean,autofocus:Boolean,value:z.any,required:Boolean},oe=$({name:"Checkbox",inheritAttrs:!1,props:H(te,{prefixCls:"rc-checkbox",type:"checkbox",defaultChecked:!1}),emits:["click","change"],setup:function(e,u){var a=u.attrs,t=u.emit,C=u.expose,p=w(e.checked===void 0?e.defaultChecked:e.checked),y=w();Q(function(){return e.checked},function(){p.value=e.checked}),R(function(){X(function(){})}),C({focus:function(){var n;(n=y.value)===null||n===void 0||n.focus()},blur:function(){var n;(n=y.value)===null||n===void 0||n.blur()}});var k=w(),s=function(n){if(!e.disabled){e.checked===void 0&&(p.value=n.target.checked),n.shiftKey=k.value;var g={target:c(c({},e),{checked:n.target.checked}),stopPropagation:function(){n.stopPropagation()},preventDefault:function(){n.preventDefault()},nativeEvent:n};e.checked!==void 0&&(y.value.checked=!!e.checked),t("change",g),k.value=!1}},_=function(n){t("click",n),k.value=n.shiftKey};return function(){var r,n=e.prefixCls,g=e.name,v=e.id,j=e.type,P=e.disabled,i=e.readonly,l=e.tabindex,h=e.autofocus,O=e.value,K=e.required,m=ne(e,["prefixCls","name","id","type","disabled","readonly","tabindex","autofocus","value","required"]),G=a.class,I=a.onFocus,N=a.onBlur,A=a.onKeydown,F=a.onKeypress,M=a.onKeyup,T=Object.keys(c(c({},m),a)).reduce(function(S,f){return(f.substr(0,5)==="aria-"||f.substr(0,5)==="data-"||f==="role")&&(S[f]=m[f]),S},{}),d=q(n,G,(r={},b(r,"".concat(n,"-checked"),p.value),b(r,"".concat(n,"-disabled"),P),r)),V=c(c({name:g,id:v,type:j,readonly:i,disabled:P,tabindex:l,class:"".concat(n,"-input"),checked:!!p.value,autofocus:h,value:O},T),{onChange:s,onClick:_,onFocus:I,onBlur:N,onKeydown:A,onKeypress:F,onKeyup:M,required:K});return x("span",{class:d},[x("input",E({ref:y},V),null),x("span",{class:"".concat(n,"-inner")},null)])}}}),re=function(){return{name:String,prefixCls:String,options:{type:Array,default:function(){return[]}},disabled:Boolean,id:String}},de=function(){return c(c({},re()),{defaultValue:{type:Array},value:{type:Array},onChange:{type:Function},"onUpdate:value":{type:Function}})},ue=function(){return{prefixCls:String,defaultChecked:{type:Boolean,default:void 0},checked:{type:Boolean,default:void 0},disabled:{type:Boolean,default:void 0},isGroup:{type:Boolean,default:void 0},value:z.any,name:String,id:String,indeterminate:{type:Boolean,default:void 0},type:{type:String,default:"checkbox"},autofocus:{type:Boolean,default:void 0},onChange:Function,"onUpdate:checked":Function,onClick:Function,skipGroup:{type:Boolean,default:!1}}},le=function(){return c(c({},ue()),{indeterminate:{type:Boolean,default:!1}})},ce=Symbol("CheckboxGroupContext"),U=globalThis&&globalThis.__rest||function(o,e){var u={};for(var a in o)Object.prototype.hasOwnProperty.call(o,a)&&e.indexOf(a)<0&&(u[a]=o[a]);if(o!=null&&typeof Object.getOwnPropertySymbols=="function")for(var t=0,a=Object.getOwnPropertySymbols(o);t<a.length;t++)e.indexOf(a[t])<0&&Object.prototype.propertyIsEnumerable.call(o,a[t])&&(u[a[t]]=o[a[t]]);return u},se=$({name:"ACheckbox",inheritAttrs:!1,__ANT_CHECKBOX:!0,props:le(),setup:function(e,u){var a=u.emit,t=u.attrs,C=u.slots,p=u.expose,y=J(),k=L("checkbox",e),s=k.prefixCls,_=k.direction,r=W(ce,void 0),n=Symbol("checkboxUniId");Y(function(){!e.skipGroup&&r&&r.registerValue(n,e.value)}),Z(function(){r&&r.cancelValue(n)}),R(function(){ee(e.checked!==void 0||r||e.value===void 0,"Checkbox","`value` is not validate prop, do you mean `checked`?")});var g=function(l){var h=l.target.checked;a("update:checked",h),a("change",l)},v=w(),j=function(){var l;(l=v.value)===null||l===void 0||l.focus()},P=function(){var l;(l=v.value)===null||l===void 0||l.blur()};return p({focus:j,blur:P}),function(){var i,l,h=ae((l=C.default)===null||l===void 0?void 0:l.call(C)),O=e.indeterminate,K=e.skipGroup,m=e.id,G=m===void 0?y.id.value:m,I=U(e,["indeterminate","skipGroup","id"]),N=t.onMouseenter,A=t.onMouseleave;t.onInput;var F=t.class,M=t.style,T=U(t,["onMouseenter","onMouseleave","onInput","class","style"]),d=c(c(c({},I),{id:G,prefixCls:s.value}),T);r&&!K?(d.onChange=function(){for(var f=arguments.length,D=new Array(f),B=0;B<f;B++)D[B]=arguments[B];a.apply(void 0,["change"].concat(D)),r.toggleOption({label:h,value:e.value})},d.name=r.name.value,d.checked=r.mergedValue.value.indexOf(e.value)!==-1,d.disabled=e.disabled||r.disabled.value,d.indeterminate=O):d.onChange=g;var V=q((i={},b(i,"".concat(s.value,"-wrapper"),!0),b(i,"".concat(s.value,"-rtl"),_.value==="rtl"),b(i,"".concat(s.value,"-wrapper-checked"),d.checked),b(i,"".concat(s.value,"-wrapper-disabled"),d.disabled),i),F),S=q(b({},"".concat(s.value,"-indeterminate"),O));return x("label",{class:V,style:M,onMouseenter:N,onMouseleave:A},[x(oe,E(E({},d),{},{class:S,ref:v}),null),h.length?x("span",null,[h]):null])}}});export{ce as C,se as a,de as c};