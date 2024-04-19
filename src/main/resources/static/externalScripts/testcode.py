from pyate import combo_basic
import sys

# source: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC1994795/
# string = """Central to the development of cancer are genetic changes that endow these “cancer cells” with many of the
# hallmarks of cancer, such as self-sufficient growth and resistance to anti-growth and pro-death signals. However, while the
# genetic changes that occur within cancer cells themselves, such as activated oncogenes or dysfunctional tumor suppressors,
# are responsible for many aspects of cancer development, they are not sufficient. Tumor promotion and progression are
# dependent on ancillary processes provided by cells of the tumor environment but that are not necessarily cancerous
# themselves. Inflammation has long been associated with the development of cancer. This review will discuss the reflexive
# relationship between cancer and inflammation with particular focus on how considering the role of inflammation in physiologic
# processes such as the maintenance of tissue homeostasis and repair may provide a logical framework for understanding the U
# connection between the inflammatory response and cancer."""

string = {sys.argv[1]}

print(combo_basic(string).sort_values(ascending=False))
""" (Output)
dysfunctional tumor                1.443147
tumor suppressors                  1.443147
genetic changes                    1.386294
cancer cells                       1.386294
dysfunctional tumor suppressors    1.298612
logical framework                  0.693147
sufficient growth                  0.693147
death signals                      0.693147
many aspects                       0.693147
inflammatory response              0.693147
tumor promotion                    0.693147
ancillary processes                0.693147
tumor environment                  0.693147
reflexive relationship             0.693147
particular focus                   0.693147
physiologic processes              0.693147
tissue homeostasis                 0.693147
cancer development                 0.693147
dtype: float64
"""