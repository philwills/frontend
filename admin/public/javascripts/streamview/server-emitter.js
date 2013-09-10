define([], function () {

    var self = this,
        hue = 0,
        group,
        Pool,
        particles,
        shaderMaterial;

    var ran = 0;

    function onParticleCreated(p) {
        //if (ran < 100) { console.log(p); ran += 1; }
        var position = p.position;
        p.target.position = position;

        //console.log(p.target);
        if (p.target) {
            particles.vertices[p.target] = position;
        }
    };

    function onParticleDead(p) {
        //particle.target.visible = false; // is this a work around?
        //group.remove(particle.target);
        shaderMaterial.attributes.customColor.value[p.target].setHSL(0, 0, 0);
        Pool.add(p.target);
    };

    var serverEmitter = function(opts) {
            Pool = opts.pool;
            particles = opts.particles;
            this.opts = opts;
            group = opts.group;
            self.hue = opts.hue;
            shaderMaterial = opts.shaderMaterial;

            //console.log(opts);

            var requestsPerSec = opts.metrics.count/60,
                counter = new SPARKS.SteadyCounter(requestsPerSec),
                sparksEmitter = new SPARKS.Emitter(counter);

            //var linezone = new SPARKS.LineZone( new THREE.Vector3(200,50,0), new THREE.Vector3(200,250,0) )
            var linezone = new SPARKS.LineZone(
                                    new THREE.Vector3(opts.pos.sx, opts.pos.sy, 0),
                                    new THREE.Vector3(opts.pos.dx, opts.pos.dy, 0)
                                )

            sparksEmitter.addInitializer(new SPARKS.Position(linezone));
            sparksEmitter.addInitializer(new SPARKS.Lifetime(1,2));
            //sparksEmitter.addInitializer(new SPARKS.Target(null, this.callback));
            sparksEmitter.addInitializer(new SPARKS.Target(null, function() {
                /*var material = new THREE.ParticleCanvasMaterial({
                                                program: SPARKS.CanvasShadersUtils.circles,
                                                blending:THREE.AdditiveBlending
                                            });
                //console.log(opts);
                material.color.setHSL(opts.hue, 1, 0.7); //0.7*/

                /*particle = new THREE.Particle( opts.shaderMaterial );

                particle.scale.x = particle.scale.y = 1.8;

                group.add( particle );

                return particle;*/
                var target = Pool.get();
                opts.shaderMaterial.attributes.customColor.value[target].setHSL(opts.hue, 1, 0.6);
                //opts.shaderMaterial.attributes.size.value[target] = Math.random() * 200 + 100;
                //values_size[target] = Math.random() * 200 + 100;

                return target;

            }));

            sparksEmitter.addInitializer(new SPARKS.Velocity(new SPARKS.PointZone(new THREE.Vector3(150,0,0))));

            sparksEmitter.addAction(new SPARKS.Age());
            sparksEmitter.addAction(new SPARKS.Move());
            sparksEmitter.addAction(new SPARKS.RandomDrift(400,400,0));
            //sparksEmitter.addAction(new SPARKS.Accelerate(0,-100,0));

            sparksEmitter.addCallback("created", onParticleCreated);
            sparksEmitter.addCallback("dead", onParticleDead);

            sparksEmitter.start();

            //console.log(sparksEmitter);
            se = sparksEmitter;

            // Add label
            var textColor = new THREE.Color(0xAAAAAA);
            var labelText = new THREE.TextGeometry(opts.title, {size: 15, font: 'optimer', height: 2,});
            var labelMesh = new THREE.Mesh(labelText, new THREE.MeshBasicMaterial({color: textColor}));
            labelMesh.position.x = opts.pos.dx;
            labelMesh.position.y = opts.pos.dy - 20;
            group.add(labelMesh);
    }

    return serverEmitter;

});