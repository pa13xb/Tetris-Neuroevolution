COSC 4P80 Final Project, Brock University
April 16, 2021
Philip Akkerman, 5479613, pa13xb@brocku.ca
David Hasler, 6041321, dh15pd@brocku.ca

Our research paper is saved as  "Neuroevolution and Genetic Algorithms with Tetris.pdf".

We developed a Tetris game, a neuroevolution neural network and a genetic algorithm to learn to play the Tetris game. The experiment results are in the ExperimentResults folder.

The Tetris game can be played by a human or the AI using the various input parameters.
The program can be run by compiling and running the Main.java class in TetrisNeuroevolution\src\Main.java. All of the source code is in the src folder.
There are many options and parameters to select. Once 0 is selected, the program is run using the set parameters and options.
- To play a human player game, ensure option 1 is true
- To play an AI game using the optimal weights we found, ensure option 17 is true and 1 is false
- To run a training experiment using the genetic algorithm, ensure option 20 is true (and 17 and 1 false)
- To run multiple (even thousands) test runs, modify the number of experiments (option 8) and ensure option 23 is true

An example run is shown below:

0: Run new game/experiments
99: Quit
Or choose a parameter to modify:
1: Human player = true
2: Display a game played by the best AI after the experiments = true
3: Max epochs = 100
4: Score goal = -1
5: Games per epoch (for unsupervised learning) = 10
6: Keep parent in population = true
7: Number of new networks per population = 10
8: Number of mutations per new network = 30
9: Modify neural network architecture. Current architecture:
Input Layer = 231
Hidden Layer 1 = 160
Hidden Layer 2 = 120
Output layer = 40
10: Number of new random networks to insert per epoch = 1
11: Number of experiment repetitions = 1
12: Include the tetromino's board position as an input = false
13: Evaluate using score (true) or time survived (false) = false
14: AI controls arrows (true) or selections positions (false) = false
15: Use supervised AI training = true
16: Error goal (for supervised learning) = 0.5
17: Use optimal moves to play a game = true
18: Number of moves per epoch (for supervised learning) = 250
19: Manually modify GA weights
20: Use a genetic algorithm to optimize weights = true
21: Specify number of elites to bring to next generation = 3
22: Run GA experiments overnight and save results = false
23: Run NN experiments overnight and save results = false
24: Run a number of experiments (games) to test a GA weight setup = false
1
0: Run new game/experiments
99: Quit
Or choose a parameter to modify:
1: Human player = false
2: Display a game played by the best AI after the experiments = true
3: Max epochs = 100
4: Score goal = -1
5: Games per epoch (for unsupervised learning) = 10
6: Keep parent in population = true
7: Number of new networks per population = 10
8: Number of mutations per new network = 30
9: Modify neural network architecture. Current architecture:
Input Layer = 231
Hidden Layer 1 = 160
Hidden Layer 2 = 120
Output layer = 40
10: Number of new random networks to insert per epoch = 1
11: Number of experiment repetitions = 1
12: Include the tetromino's board position as an input = false
13: Evaluate using score (true) or time survived (false) = false
14: AI controls arrows (true) or selections positions (false) = false
15: Use supervised AI training = true
16: Error goal (for supervised learning) = 0.5
17: Use optimal moves to play a game = true
18: Number of moves per epoch (for supervised learning) = 250
19: Manually modify GA weights
20: Use a genetic algorithm to optimize weights = true
21: Specify number of elites to bring to next generation = 3
22: Run GA experiments overnight and save results = false
23: Run NN experiments overnight and save results = false
24: Run a number of experiments (games) to test a GA weight setup = false
17
0: Run new game/experiments
99: Quit
Or choose a parameter to modify:
1: Human player = false
2: Display a game played by the best AI after the experiments = true
3: Max epochs = 100
4: Score goal = -1
5: Games per epoch (for unsupervised learning) = 10
6: Keep parent in population = true
7: Number of new networks per population = 10
8: Number of mutations per new network = 30
9: Modify neural network architecture. Current architecture:
Input Layer = 231
Hidden Layer 1 = 160
Hidden Layer 2 = 120
Output layer = 40
10: Number of new random networks to insert per epoch = 1
11: Number of experiment repetitions = 1
12: Include the tetromino's board position as an input = false
13: Evaluate using score (true) or time survived (false) = false
14: AI controls arrows (true) or selections positions (false) = false
15: Use supervised AI training = true
16: Error goal (for supervised learning) = 0.5
17: Use optimal moves to play a game = false
18: Number of moves per epoch (for supervised learning) = 250
19: Manually modify GA weights
20: Use a genetic algorithm to optimize weights = true
21: Specify number of elites to bring to next generation = 3
22: Run GA experiments overnight and save results = false
23: Run NN experiments overnight and save results = false
24: Run a number of experiments (games) to test a GA weight setup = false
13
0: Run new game/experiments
99: Quit
Or choose a parameter to modify:
1: Human player = false
2: Display a game played by the best AI after the experiments = true
3: Max epochs = 100
4: Score goal = -1
5: Games per epoch (for unsupervised learning) = 10
6: Keep parent in population = true
7: Number of new networks per population = 10
8: Number of mutations per new network = 30
9: Modify neural network architecture. Current architecture:
Input Layer = 231
Hidden Layer 1 = 160
Hidden Layer 2 = 120
Output layer = 40
10: Number of new random networks to insert per epoch = 1
11: Number of experiment repetitions = 1
12: Include the tetromino's board position as an input = false
13: Evaluate using score (true) or time survived (false) = true
14: AI controls arrows (true) or selections positions (false) = false
15: Use supervised AI training = true
16: Error goal (for supervised learning) = 0.5
17: Use optimal moves to play a game = false
18: Number of moves per epoch (for supervised learning) = 250
19: Manually modify GA weights
20: Use a genetic algorithm to optimize weights = true
21: Specify number of elites to bring to next generation = 3
22: Run GA experiments overnight and save results = false
23: Run NN experiments overnight and save results = false
24: Run a number of experiments (games) to test a GA weight setup = false
3
Enter max epochs:
30
0: Run new game/experiments
99: Quit
Or choose a parameter to modify:
1: Human player = false
2: Display a game played by the best AI after the experiments = true
3: Max epochs = 30
4: Score goal = -1
5: Games per epoch (for unsupervised learning) = 10
6: Keep parent in population = true
7: Number of new networks per population = 10
8: Number of mutations per new network = 30
9: Modify neural network architecture. Current architecture:
Input Layer = 231
Hidden Layer 1 = 160
Hidden Layer 2 = 120
Output layer = 40
10: Number of new random networks to insert per epoch = 1
11: Number of experiment repetitions = 1
12: Include the tetromino's board position as an input = false
13: Evaluate using score (true) or time survived (false) = true
14: AI controls arrows (true) or selections positions (false) = false
15: Use supervised AI training = true
16: Error goal (for supervised learning) = 0.5
17: Use optimal moves to play a game = false
18: Number of moves per epoch (for supervised learning) = 250
19: Manually modify GA weights
20: Use a genetic algorithm to optimize weights = true
21: Specify number of elites to bring to next generation = 3
22: Run GA experiments overnight and save results = false
23: Run NN experiments overnight and save results = false
24: Run a number of experiments (games) to test a GA weight setup = false
5
Enter games per epoch:
20
0: Run new game/experiments
99: Quit
Or choose a parameter to modify:
1: Human player = false
2: Display a game played by the best AI after the experiments = true
3: Max epochs = 30
4: Score goal = -1
5: Games per epoch (for unsupervised learning) = 20
6: Keep parent in population = true
7: Number of new networks per population = 10
8: Number of mutations per new network = 30
9: Modify neural network architecture. Current architecture:
Input Layer = 231
Hidden Layer 1 = 160
Hidden Layer 2 = 120
Output layer = 40
10: Number of new random networks to insert per epoch = 1
11: Number of experiment repetitions = 1
12: Include the tetromino's board position as an input = false
13: Evaluate using score (true) or time survived (false) = true
14: AI controls arrows (true) or selections positions (false) = false
15: Use supervised AI training = true
16: Error goal (for supervised learning) = 0.5
17: Use optimal moves to play a game = false
18: Number of moves per epoch (for supervised learning) = 250
19: Manually modify GA weights
20: Use a genetic algorithm to optimize weights = true
21: Specify number of elites to bring to next generation = 3
22: Run GA experiments overnight and save results = false
23: Run NN experiments overnight and save results = false
24: Run a number of experiments (games) to test a GA weight setup = false
7
Enter number of new networks per population:
20
0: Run new game/experiments
99: Quit
Or choose a parameter to modify:
1: Human player = false
2: Display a game played by the best AI after the experiments = true
3: Max epochs = 30
4: Score goal = -1
5: Games per epoch (for unsupervised learning) = 20
6: Keep parent in population = true
7: Number of new networks per population = 20
8: Number of mutations per new network = 30
9: Modify neural network architecture. Current architecture:
Input Layer = 231
Hidden Layer 1 = 160
Hidden Layer 2 = 120
Output layer = 40
10: Number of new random networks to insert per epoch = 1
11: Number of experiment repetitions = 1
12: Include the tetromino's board position as an input = false
13: Evaluate using score (true) or time survived (false) = true
14: AI controls arrows (true) or selections positions (false) = false
15: Use supervised AI training = true
16: Error goal (for supervised learning) = 0.5
17: Use optimal moves to play a game = false
18: Number of moves per epoch (for supervised learning) = 250
19: Manually modify GA weights
20: Use a genetic algorithm to optimize weights = true
21: Specify number of elites to bring to next generation = 3
22: Run GA experiments overnight and save results = false
23: Run NN experiments overnight and save results = false
24: Run a number of experiments (games) to test a GA weight setup = false
0
====================================
Experiment #1 of 1 beginning
====================================
Epoch 1 complete, best network's average score = 2264.0
0.6855432245042565	0.140093462478472	0.4328068329336039	0.9464829516462017	0.3865318172027854	0.9065295369147117	0.09143385028811601	0.08468769815581156	-0.976723207968972	0.5710150981330169	-0.12631820826275608	0.8211253477997527
Epoch 2 complete, best network's average score = 2457.0
0.6855432245042565	0.140093462478472	0.4328068329336039	0.9464829516462017	0.3865318172027854	0.9065295369147117	0.09143385028811601	0.08468769815581156	-0.976723207968972	0.5710150981330169	-0.12631820826275608	0.8211253477997527
Epoch 3 complete, best network's average score = 2462.0
0.6855432245042565	0.140093462478472	0.4328068329336039	0.9464829516462017	0.3865318172027854	0.9065295369147117	0.09143385028811601	0.08468769815581156	-0.976723207968972	0.5710150981330169	-0.12631820826275608	0.8211253477997527
Epoch 4 complete, best network's average score = 1901.0
0.6855432245042565	0.140093462478472	0.4328068329336039	0.9464829516462017	0.3865318172027854	0.9065295369147117	0.09143385028811601	0.08468769815581156	-0.976723207968972	0.5710150981330169	-0.12631820826275608	0.8211253477997527
Epoch 5 complete, best network's average score = 2191.0
0.6855432245042565	0.140093462478472	0.4328068329336039	0.9464829516462017	0.3865318172027854	0.9065295369147117	0.09143385028811601	0.08468769815581156	-0.976723207968972	0.5710150981330169	-0.12631820826275608	0.8211253477997527
Epoch 6 complete, best network's average score = 2092.0
0.6855432245042565	0.140093462478472	0.4328068329336039	0.9464829516462017	0.3865318172027854	0.9065295369147117	0.09143385028811601	0.08468769815581156	-0.976723207968972	0.5710150981330169	-0.12631820826275608	0.8211253477997527
Epoch 7 complete, best network's average score = 2098.0
8.070252042215506	-2.513018846658018	1.303460775243424	0.8730685148376417	0.09702220254665583	1.0558727058208817	0.4901053509192197	0.03652184355493915	-0.976723207968972	0.11795382837287784	-0.12631820826275608	-0.1790784079465578
Epoch 8 complete, best network's average score = 2381.0
8.070252042215506	-2.513018846658018	1.303460775243424	0.8730685148376417	0.09702220254665583	1.0558727058208817	0.4901053509192197	0.03652184355493915	-0.976723207968972	0.11795382837287784	-0.12631820826275608	-0.1790784079465578
Epoch 9 complete, best network's average score = 2237.0
-0.277196966727536	-0.22775813582657606	9.967995491259247	1.8895672937341186	-0.23946257310705077	1.907994553626005	1.4035593968702038	0.03652184355493915	0.7954372150843332	-0.30709818837639336	1.2056989077432312	-4.894986947897062
Epoch 10 complete, best network's average score = 2903.0
-0.277196966727536	-0.22775813582657606	9.967995491259247	1.8895672937341186	-0.23946257310705077	1.907994553626005	1.4035593968702038	0.03652184355493915	0.7954372150843332	-0.30709818837639336	1.2056989077432312	-4.894986947897062
Epoch 11 complete, best network's average score = 2460.0
-0.277196966727536	-0.22775813582657606	9.967995491259247	1.8895672937341186	-0.23946257310705077	1.907994553626005	1.4035593968702038	0.03652184355493915	0.7954372150843332	-0.30709818837639336	1.2056989077432312	-4.894986947897062
Epoch 12 complete, best network's average score = 2752.0
-0.277196966727536	-0.22775813582657606	9.967995491259247	1.8895672937341186	-0.23946257310705077	1.907994553626005	1.4035593968702038	0.03652184355493915	0.7954372150843332	-0.30709818837639336	1.2056989077432312	-4.894986947897062
Epoch 13 complete, best network's average score = 2682.0
-4.01266032863826	-0.8682233492486258	9.967995491259247	11.610341693957738	-42.975361704667804	0.19414719214648746	0.6976046847811621	0.158580970591956	2.5181236151393347	-1.125448148809848	0.9761841931123847	-1.7396424021947585
Epoch 14 complete, best network's average score = 2804.0
-0.277196966727536	-0.22775813582657606	9.967995491259247	1.8895672937341186	-0.23946257310705077	1.907994553626005	1.4035593968702038	0.03652184355493915	0.7954372150843332	-0.30709818837639336	1.2056989077432312	-4.894986947897062
Epoch 15 complete, best network's average score = 2794.0
0.9516627972874083	-0.2983565952866556	504.65015840842676	1.6088758142968747	1.356364403265728	7.5922849776591175	0.09002604938305747	0.03652184355493915	-0.5407565890036962	0.4369953553083812	0.4279745853587934	-0.8169186537114757
Epoch 16 complete, best network's average score = 3008.0
5.716241531418789	-1.0402881081281667	46.897773450332735	0.9062084880875728	0.2886002330151746	1.907994553626005	0.9532716575143882	-1.0317707000105294	0.7954372150843332	0.5981809828676092	0.8620378473247912	-4.894986947897062
Epoch 17 complete, best network's average score = 2947.0
-0.041207425487870646	27.042587816781676	503.8598564628216	1.4019764257858511	-8.977098842744322	37.57348307995749	-0.026690581453349627	0.03652184355493915	-0.23757543286352126	-0.2621962873639232	-1.3271724095630901	-1.0101431242644534
Epoch 18 complete, best network's average score = 2874.0
5.716241531418789	-1.0402881081281667	46.897773450332735	0.9062084880875728	0.2886002330151746	1.907994553626005	0.9532716575143882	-1.0317707000105294	0.7954372150843332	0.5981809828676092	0.8620378473247912	-4.894986947897062
Epoch 19 complete, best network's average score = 2803.0
-0.041207425487870646	27.042587816781676	503.8598564628216	1.4019764257858511	-8.977098842744322	37.57348307995749	-0.026690581453349627	0.03652184355493915	-0.23757543286352126	-0.2621962873639232	-1.3271724095630901	-1.0101431242644534
Epoch 20 complete, best network's average score = 2806.0
5.716241531418789	-1.0402881081281667	46.897773450332735	0.9062084880875728	0.2886002330151746	1.907994553626005	0.9532716575143882	-1.0317707000105294	0.7954372150843332	0.5981809828676092	0.8620378473247912	-4.894986947897062
Epoch 21 complete, best network's average score = 2791.0
5.716241531418789	-1.0402881081281667	46.897773450332735	0.9062084880875728	0.2886002330151746	1.907994553626005	0.9532716575143882	-1.0317707000105294	0.7954372150843332	0.5981809828676092	0.8620378473247912	-4.894986947897062
Epoch 22 complete, best network's average score = 3027.0
-0.44508556335258637	-2.025178845579746	691.3130164411542	4.924656022421134	0.3560037616666665	37.57348307995749	-1.051983170018226	-0.39970016404836883	0.09159097591373544	0.365370947051032	-2.0336504706927454	-1.2430518022235442
Epoch 23 complete, best network's average score = 3284.0
-4.9613040389120835	0.42801585686458155	982.1441410991874	4.403676404770651	0.4748609174912741	37.57348307995749	0.8339103128620198	-13.629091180703341	-0.6833211548320824	-0.10656657918621383	-0.4527276562918263	-1.2430518022235442
Epoch 24 complete, best network's average score = 3164.0
5.716241531418789	-1.0402881081281667	46.897773450332735	0.9062084880875728	0.2886002330151746	1.907994553626005	0.9532716575143882	-1.0317707000105294	0.7954372150843332	0.5981809828676092	0.8620378473247912	-4.894986947897062
Epoch 25 complete, best network's average score = 2945.0
0.31685654056315404	-0.5030465618749616	10.710808495720988	0.5049020036179462	-1.085131630555506	1.0691010762733462	1.6505567351729673	-4.959082283252735	-0.009959088643204717	1.3245329296892923	6.894854260553261	-0.6991804112136277
Epoch 26 complete, best network's average score = 3410.0
-0.9083873670436118	-1.0057878486123009	11.7693024274136	0.8099835821848076	-2.4851327500335336	1.0691010762733462	0.6404088651074415	-5.453159768710992	-0.6693884601287303	1.4443541569088152	0.46232240182778267	48.01317735714692
Epoch 27 complete, best network's average score = 4070.0
-0.42305266529774066	-1.1536772397786699	10.94838875443189	1.3913298538973342	0.13881147639919172	1.0691010762733462	0.3830850416151683	-13.64508275051152	-0.21224186222396083	0.5111767177412402	-0.6442276826119637	78.49080740388693
Epoch 28 complete, best network's average score = 3835.0
-0.42305266529774066	-1.1536772397786699	10.94838875443189	1.3913298538973342	0.13881147639919172	1.0691010762733462	0.3830850416151683	-13.64508275051152	-0.21224186222396083	0.5111767177412402	-0.6442276826119637	78.49080740388693
Epoch 29 complete, best network's average score = 4622.0
-0.42305266529774066	-1.1536772397786699	10.94838875443189	1.3913298538973342	0.13881147639919172	1.0691010762733462	0.3830850416151683	-13.64508275051152	-0.21224186222396083	0.5111767177412402	-0.6442276826119637	78.49080740388693
Epoch 30 complete, best network's average score = 5192.0
Printing results now:
====================================

2264.0
2457.0
2462.0
1901.0
2191.0
2092.0
2098.0
2381.0
2237.0
2903.0
2460.0
2752.0
2682.0
2804.0
2794.0
3008.0
2947.0
2874.0
2803.0
2806.0
2791.0
3027.0
3284.0
3164.0
2945.0
3410.0
4070.0
3835.0
4622.0
5192.0


====================================

Best score achieved = 18240

--> A game proceeds to run using the weights calculated by the GA

Cheers,

Philip and David