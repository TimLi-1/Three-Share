/**
 * The log/js is used for formatting the logs 
 * @param {*} type 
 * @param {*} infos 
 */
function log(type, infos) {
    var msg = new Date().toISOString() + '\t' + type;
    if (infos) msg += '\t' + JSON.stringify(infos);
    console.log(msg);
}

module.exports.log = log;