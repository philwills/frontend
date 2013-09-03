define(function () {

    var self = this,
        hue = 0,
        group;


    function onParticleCreated(p) {
        var position = p.position;
        p.target.position = position;
    };

    function onParticleDead(particle) {
        particle.target.visible = false; // is this a work around?
        group.remove(particle.target);
    };

    var serverEmitter = function(opts) {
            this.opts = opts;
            group = opts.group;
            self.hue = opts.hue;

            //console.log(opts);

            var requestsPerSec = opts.metrics.count/60,
                sparksEmitter = new SPARKS.Emitter(new SPARKS.SteadyCounter(requestsPerSec));

            //var linezone = new SPARKS.LineZone( new THREE.Vector3(200,50,0), new THREE.Vector3(200,250,0) )
            var linezone = new SPARKS.LineZone(
                                    new THREE.Vector3(opts.pos.sx, opts.pos.sy, 0),
                                    new THREE.Vector3(opts.pos.dx, opts.pos.dy, 0)
                                )

            sparksEmitter.addInitializer(new SPARKS.Position(linezone));
            sparksEmitter.addInitializer(new SPARKS.Lifetime(1,2));
            //sparksEmitter.addInitializer(new SPARKS.Target(null, this.callback));
            sparksEmitter.addInitializer(new SPARKS.Target(null, function() {
                var material = new THREE.ParticleCanvasMaterial({
                                                program: SPARKS.CanvasShadersUtils.circles,
                                                blending:THREE.AdditiveBlending
                                            });
                //console.log(opts);
                material.color.setHSL(opts.hue, 1, 0.7); //0.7

                particle = new THREE.Particle( material );

                particle.scale.x = particle.scale.y = 1.4;

                group.add( particle );

                return particle;
            }));

            sparksEmitter.addInitializer(new SPARKS.Velocity(new SPARKS.PointZone(new THREE.Vector3(200,0,0))));

            sparksEmitter.addAction(new SPARKS.Age());
            sparksEmitter.addAction(new SPARKS.Move());
            sparksEmitter.addAction(new SPARKS.RandomDrift(500,500,0));
            //sparksEmitter.addAction(new SPARKS.Accelerate(0,-100,0));

            sparksEmitter.addCallback("created", onParticleCreated);
            sparksEmitter.addCallback("dead", onParticleDead);

            sparksEmitter.start();

    }

    return serverEmitter;

});